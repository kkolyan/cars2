package com.nplekhanov.cars2.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author nplekhanov
 */
public class SearchRunner {

    public static <T extends MeaningfulTextProvider> void parse(ParsingStrategy<T> strategy) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        context.setAttribute(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
        context.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());

        String folder = "dumps/"+new SimpleDateFormat("yyyy.MM.dd_HHmmss_SSS").format(new Date());
        new File(folder).mkdirs();

        ObjectMapper mapper = new ObjectMapper();
        try (OutputStream target = new BufferedOutputStream(new FileOutputStream(new File(folder, "parsed.txt")))) {

            for (T param: strategy.getParams()) {
                for (int p = 1; p < 200; p ++) {
                    System.out.print(new Date()+", param: " + param + ", page: " + p);
                    String url = strategy.createUrl(param, p);
                    String html = request(url, client, context, strategy.getReferer());
                    Collection<? extends ShortDescription> list;
                    try {
                        Document document = Jsoup.parse(html);
                        list = strategy.parse(document, param);
                        if (!list.isEmpty()) {
                            File file = new File(folder, param.asMeaningFulText()+"."+String.format("%03d", p)+".html");
                            file.getParentFile().mkdirs();
                            FileUtils.write(file, html);
                        }
                    } catch (Exception e) {
                        System.out.println(html);
                        throw new IllegalStateException(e);
                    }
                    System.out.println(", fetched "+list.size()+" offers, "+Lambda.filter(list, new Predicate<ShortDescription>() {
                        @Override
                        public boolean apply(ShortDescription o) {
                            return o.getException() != null;
                        }
                    }).size()+" of them failed");
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
                        break;
                    }
                }

            }
        }



    }

    static String request(String url, HttpClient client, HttpContext context, String referer) {
        if (url.startsWith("file:")) {
            try {
                return FileUtils.readFileToString(new File(url.substring("file:".length())));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        int attemptsLeft = 10;
        while (true) {
            String html;
            HttpGet request = new HttpGet(url);
            request.setHeader("Referer", referer);
            HttpResponse response;
            try {
                response = client.execute(request, context);
            } catch (ClientProtocolException e) {
                throw new IllegalStateException(e);
            } catch (IOException e) {
                try {
                    request.releaseConnection();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (attemptsLeft > 0) {
                    System.err.println("attempt failed with "+e);
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
                try {
                    request.releaseConnection();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
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
