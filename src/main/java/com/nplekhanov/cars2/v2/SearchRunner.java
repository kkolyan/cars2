package com.nplekhanov.cars2.v2;

import com.google.common.base.Predicate;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.*;

/**
 * @author nplekhanov
 */
public class SearchRunner {
    private static Bookmark bookmark
            ;
//            = new Bookmark("Cadillac", 2012, 1);

    public static void main(String[] args) throws IOException {

    }
    public static void parse(ParsingStrategy strategy) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        context.setAttribute(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
        context.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());

        try (OutputStream target = new BufferedOutputStream(new FileOutputStream("results."+System.currentTimeMillis()+".txt"))) {

            for (int i = 0; i < 20; i ++) {
                int year = 2015 - i;
                for (Mark mark: strategy.getMarks()) {
                    for (int p = 1; p < 200; p ++) {
                        if (bookmark != null) {
                            if (!bookmark.equals(new Bookmark(mark.getMarkTitle(), year, p))) {
                                continue;
                            }
                            bookmark = null;
                        }
                        System.out.print(new Date()+", mark: " + mark.getMarkTitle() + ", year: " + year + ", page: " + p);
                        String url = strategy.createUrl(mark, year, p);
                        String html = request(url, client, context, strategy);
                        Collection<? extends ShortDescription> list;
                        try {
                            Document document = Jsoup.parse(html);
                            list = strategy.parse(document, mark);
                        } catch (Exception e) {
                            System.out.println(html);
                            throw new IllegalStateException(e);
                        }
                        System.out.println(", fetched "+list.size()+" offers, "+Lambda.filter(list, new Predicate<ShortDescription>() {
                            @Override
                            public boolean apply(ShortDescription o) {
                                return o.getException() != null;
                            }
                        })+" of them failed");
                        if (list.isEmpty()) {
                            break;
                        }
                        for (ShortDescription o: list) {
                            try {
                                target.write(o.toJson().getBytes());
                                target.write("\r\n".getBytes());
                                target.flush();
                            } catch (Exception e) {
                                throw new IllegalStateException(e);
                            }
                        }

                        try {
                            Thread.sleep(5000+new Random().nextInt(10000));
                        } catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }

            }
        }



    }

    private static String request(String url, HttpClient client, HttpContext context, ParsingStrategy strategy) {

        int attemptsLeft = 10;
        while (true) {
            String html;
            HttpUriRequest request = new HttpGet(url);
            request.setHeader("Referer", strategy.getReferer());
            HttpResponse response;
            try {
                response = client.execute(request, context);
            } catch (IOException e) {
                if (attemptsLeft > 0) {
                    System.out.println("attempt failed with "+e);
                    attemptsLeft --;
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e1) {
                        throw new IllegalStateException(e1);
                    }
                    continue;
                }
                throw new IllegalStateException(e);
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            try {
                response.getEntity().writeTo(buffer);
                html = buffer.toString();
            } catch (Exception e) {
                if (attemptsLeft > 0) {
                    System.out.println(e);
                    attemptsLeft --;
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e1) {
                        throw new IllegalStateException(e1);
                    }
                    continue;
                }
                System.out.println(buffer);
                throw new IllegalStateException(e);
            }
            return html;
        }
    }
}
