package com.nplekhanov.cars2.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.nplekhanov.cars2.v2.auto2.Duration;
import com.nplekhanov.cars2.v2.auto2.SummaryItem;
import com.nplekhanov.cars2.v2.auto2.SummaryParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import static com.nplekhanov.cars2.v2.SearchRunner.*;

/**
 * @author nplekhanov
 */
public class SummaryBasedSearchRunner {
    static int doneByMark;
    static String referer;

    static Bookmark bookmark;
//    = new Bookmark("Citroen", "C4", 32);

    public static void parse(SummaryBasedParsingStrategy strategy) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        context.setAttribute(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
        context.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());

        String folder = "dumps/"+new SimpleDateFormat("yyyy.MM.dd_HHmmss_SSS").format(new Date());
        new File(folder).mkdirs();

        referer = null;

        long startedAt = System.currentTimeMillis();

        ObjectMapper mapper = new ObjectMapper();
        try (OutputStream target = new BufferedOutputStream(new FileOutputStream(new File(folder, "parsed.txt")))) {

            String usedSummaryHtml = request(strategy.getUsedSummaryUrl(), client, context, referer);
            FileUtils.write(new File(folder, "used_summary.html"), usedSummaryHtml);

            referer = strategy.getUsedSummaryUrl();

            Collection<SummaryItem> usedSummary = SummaryParser.parseUsedSummary(Jsoup.parse(usedSummaryHtml));

            int total = 0;
            for (SummaryItem mark: usedSummary) {
                total += mark.getCount();
            }

            int doneTotal = 0;

            for (SummaryItem mark: usedSummary) {
                if (mark.getCount() <= 0) {
                    continue;
                }

                if (bookmark != null && !bookmark.getMark().equals(mark.getTitle())) {
                    continue;
                }
                String markSummaryUrl = strategy.getUsedMarkSummaryUrl(mark);
                String markSummaryHtml = request(markSummaryUrl, client, context, referer);
                FileUtils.write(new File(new File(folder, mark.getTitle()), "used_mark_summary.html"), markSummaryHtml);

                referer = markSummaryUrl;

                Collection<SummaryItem> markSummary = SummaryParser.parseUsedMarkSummary(Jsoup.parse(markSummaryHtml));
                doneByMark = 0;

                for (SummaryItem model: markSummary) {
                    if (model.getCount() <= 0) {
                        continue;
                    }
                    if (bookmark != null && !bookmark.getModel().equals(model.getTitle())) {
                        continue;
                    }

                    for (int p = 1; p < 200; p ++) {
                        if (bookmark != null && bookmark.getPage() > p) {
                            continue;
                        }
                        bookmark = null;

                        if (doPage(strategy, client, context, folder, startedAt, mapper, target, total, doneTotal, mark, model, p))
                            break;
                    }
                }
                doneTotal += mark.getCount();
            }
        }

    }

    private static boolean doPage(SummaryBasedParsingStrategy strategy, HttpClient client, HttpContext context, String folder, long startedAt, ObjectMapper mapper, OutputStream target, int total, int doneTotal, SummaryItem mark, SummaryItem model, int p) {
        String messageBegin = new Date() + ", mark: " + mark.getTitle() + ", model: " + model.getTitle() + ", page: " + p;
        System.out.print(messageBegin);
        try {
            FileUtils.write(new File("logs/SummaryBasedSearchRunner.log"), messageBegin, true);
        } catch (IOException e) {
            System.err.println(e);
        }
        String url = strategy.createUrl(model, p);
        String html = null;
        try {
            html = request(url, client, context, referer);
        } catch (Exception e) {

            String message = " failed to send request to url "+url+" ("+mark+","+model+", "+p+") due to following error: "+ ExceptionUtils.getFullStackTrace(e);
            System.out.println(message);
            try {
                FileUtils.write(new File("logs/SummaryBasedSearchRunner.log"), message);
            } catch (IOException e1) {
                e.printStackTrace();
            }
            return true;
        }
        referer = url;
        Collection<? extends ShortDescription> list;
        try {
            Document document = Jsoup.parse(html);
            list = strategy.parse(document, mark, model);
            doneByMark += list.size();
            if (!list.isEmpty()) {
                File file = getFileForPage(folder, mark, p);
//                System.out.println(file.getAbsolutePath());
                file.getParentFile().mkdirs();
                FileUtils.write(file, html);
            }
        } catch (Exception e) {
            System.out.println(html);
            throw new IllegalStateException(e);
        }
        double progress = 1.0 * (doneTotal + doneByMark) / total;

        long eta = (long) ((1 - progress) * (System.currentTimeMillis() - startedAt) / progress);
        String messageEnd = ", fetched " + list.size() + " offers, " + Lambda.filter(list, new Predicate<ShortDescription>() {
            @Override
            public boolean apply(ShortDescription o) {
                return o.getException() != null;
            }
        }).size() + " of them failed" + ", " + String.format("%.02f", 100.0 * progress) + "% (" + (doneTotal + doneByMark) + "/" + total + "), ETA: " + new Duration(eta).format();
        System.out.println(messageEnd);
        try {
            FileUtils.write(new File("logs/SummaryBasedSearchRunner.log"), messageEnd+"\r\n", true);
        } catch (IOException e) {
            System.err.println(e);
        }
        for (ShortDescription o: list) {
            try {
                target.write(mapper.writeValueAsString(o).getBytes());
                target.write("\r\n".getBytes());
                target.flush();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        try {
            Thread.sleep(strategy.getPauseMillis());
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        if (list.size() < strategy.getEntriesPerPageThreshold()) {
            return true;
        }
        return false;
    }

    private static File getFileForPage(String folder, SummaryItem mark, int p) {
        return new File(new File(folder, mark.getTitle()), mark.getTitle()+"."+String.format("%03d", p)+".html");
    }


    private static class Bookmark {
        private String mark;
        private String model;
        private int page;

        private Bookmark(String mark, String model, int page) {
            this.mark = mark;
            this.model = model;
            this.page = page;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Bookmark bookmark = (Bookmark) o;

            if (page != bookmark.page) return false;
            if (mark != null ? !mark.equals(bookmark.mark) : bookmark.mark != null) return false;
            if (model != null ? !model.equals(bookmark.model) : bookmark.model != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = mark != null ? mark.hashCode() : 0;
            result = 31 * result + (model != null ? model.hashCode() : 0);
            result = 31 * result + page;
            return result;
        }

        public String getMark() {
            return mark;
        }

        public String getModel() {
            return model;
        }

        public int getPage() {
            return page;
        }
    }
}
