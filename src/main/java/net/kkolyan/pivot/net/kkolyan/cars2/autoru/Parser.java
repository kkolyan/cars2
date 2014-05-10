package net.kkolyan.pivot.net.kkolyan.cars2.autoru;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class Parser {
    public static Collection<Map<String,Object>> parse(Document document, String mark, Date now) {

        if (document.select("h1").text().contains("Ваш IP адрес временно заблокирован")) {
            throw new IllegalStateException(document.select("h1").text());
        }

        Elements rows = document.select("table.list tbody tr");
        if (rows.isEmpty() || rows.size() == 1) {
            return new ArrayList<Map<String, Object>>(0);
        }
        Collection<Map<String,Object>> offers = new ArrayList<Map<String,Object>>();
        for (Element row: rows.subList(1, rows.size())) {
            Elements cells = row.select("td");
            Map<String,Object> offer = new HashMap<String, Object>();
            offer.put("mark", mark);
            offer.put("model", cells.get(0).text().trim());
            offer.put("url", cells.get(0).select("a").attr("href").trim());
            offer.put("price", Integer.parseInt(cells.get(1).text().replace(" ", "")));
            offer.put("year", Integer.parseInt(cells.get(2).text().replace(" ", "")));
            offer.put("engine", cells.get(3).text().trim());
            offer.put("type", cells.get(4).text().trim());
            offer.put("right_hand", !cells.get(4).select("img[alt=Правый руль]").isEmpty());
            offer.put("running", Integer.parseInt(cells.get(5).text().replace(" ", "")));
            offer.put("photo", !cells.get(6).select("img").isEmpty());
            offer.put("color", cells.get(8).select("div").get(0).attr("title").trim());
            offer.put("city", cells.get(9).text().trim());
            offer.put("availability", cells.get(11).text().trim());
            offer.put("parsed_at", now);
            offers.add(offer);
        }

        return offers;
    }

    public static void main(String[] args) throws IOException {

        String html = new String(FileCopyUtils.copyToByteArray(new File("D:/dev/cars2/toyota.htm")));
        Document document = Jsoup.parse(html);
        for (Map<String,Object> offer: parse(document, "Toyota", new Date())) {
            System.out.println(offer);
        }
    }
}
