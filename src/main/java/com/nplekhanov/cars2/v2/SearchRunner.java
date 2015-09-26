package com.nplekhanov.cars2.v2;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author nplekhanov
 */
public class SearchRunner {
    private static Bookmark bookmark = new Bookmark("Mercedes-Benz", 2011, 3);

    public static void main(String[] args) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        context.setAttribute(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
        context.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());

        try (OutputStream target = new BufferedOutputStream(new FileOutputStream("results."+System.currentTimeMillis()+".txt"))) {

            IntStream.range(1, 19).forEach(i -> {
                int year = 2015 - i;
                Marks.getMarks().forEach((markId, markTitle) -> {
                    String path = Paths.getPaths().get(markTitle);
                    if (path == null) {
                        System.out.println("path for mark not found: "+markTitle);
                    }
                    for (int p = 1; p < 200; p ++) {
                        if (bookmark != null) {
                            if (!bookmark.equals(new Bookmark(markTitle, year, p))) {
                                continue;
                            }
                            bookmark = null;
                        }
                        System.out.print(LocalDateTime.now().toString().replace("T", " ")+", mark: " + markTitle + ", year: " + year + ", page: " + p);
                        String url = "" +
                                "http://auto.ru" + path +
                                "?search%5Bsection_id%5D=0" +
                                "&search%5Bmark%5D%5B0%5D=" + markId +
                                "&search%5Bmark-folder%5D%5B0%5D="+markId+"-0" +
                                "&search%5Bsalon_id%5D=" +
                                "&search%5Byear%5D%5Bmin%5D=" + year +
                                "&search%5Byear%5D%5Bmax%5D=" + year +
                                "&search%5Bprice%5D%5Bmin%5D=" +
                                "&search%5Bprice%5D%5Bmax%5D=" +
                                "&search%5Bengine_volume%5D%5Bmin%5D=" +
                                "&search%5Bengine_volume%5D%5Bmax%5D=" +
                                "&search%5Brun%5D%5Bmax%5D=" +
                                "&search%5Bengine_power%5D%5Bmin%5D=" +
                                "&search%5Bengine_power%5D%5Bmax%5D=" +
                                "&search%5Bcustom%5D=1" +
                                "&search%5Bacceleration%5D%5Bmin%5D=" +
                                "&search%5Bacceleration%5D%5Bmax%5D=" +
                                "&search%5Bstate%5D=1" +
                                "&search%5Bgeo_region%5D=32%2C89" +
                                "&search%5Bgeo_city%5D=" +
                                "&search%5Bgeo_country%5D=" +
                                "&search%5Bgeo_similar_cities%5D=" +
                                "&search%5Bperiod%5D=0" +
                                "&show_sales=1";
                        if (p > 1) {
                            url += "&p="+p;
                        }
                        String html = request(url, client, context);
                        List<ShortDescription> list;
                        try {
                            Document document = Jsoup.parse(html);
                            list = ShortDescription.parse(document, markTitle);
                        } catch (Exception e) {
                            System.out.println(html);
                            throw new IllegalStateException(e);
                        }
                        System.out.println(", fetched "+list.size()+" offers, "+list.stream().filter(o->o.getException() != null).count()+" of them failed");
                        if (list.isEmpty()) {
                            break;
                        }
                        list.forEach(o -> {
                            try {
                                target.write(o.toJson().getBytes());
                                target.write("\r\n".getBytes());
                                target.flush();
                            } catch (Exception e) {
                                throw new IllegalStateException(e);
                            }
                        });

                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                    }

                });
            });
        }



    }

    private static String request(String url, HttpClient client, HttpContext context) {

        int attemptsLeft = 10;
        while (true) {
            String html;
            HttpUriRequest request = new HttpGet(url);
            request.setHeader("Referer", "http://auto.ru/cars/mercedes/all/?search%5Bsection_id%5D=0&search%5Bmark%5D%5B0%5D=170&search%5Bmark-folder%5D%5B0%5D=170-0&search%5Bsalon_id%5D=&search%5Byear%5D%5Bmin%5D=2015&search%5Byear%5D%5Bmax%5D=2015&search%5Bprice%5D%5Bmin%5D=&search%5Bprice%5D%5Bmax%5D=&search%5Bengine_volume%5D%5Bmin%5D=&search%5Bengine_volume%5D%5Bmax%5D=&search%5Brun%5D%5Bmax%5D=&search%5Bengine_power%5D%5Bmin%5D=&search%5Bengine_power%5D%5Bmax%5D=&search%5Bcustom%5D=1&search%5Bacceleration%5D%5Bmin%5D=&search%5Bacceleration%5D%5Bmax%5D=&search%5Bstate%5D=1&search%5Bgeo_region%5D=32%2C89&search%5Bgeo_city%5D=&search%5Bgeo_country%5D=&search%5Bgeo_similar_cities%5D=&search%5Bperiod%5D=0&show_sales=1&p=2");
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
