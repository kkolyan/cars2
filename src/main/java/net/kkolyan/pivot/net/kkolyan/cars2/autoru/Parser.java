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

/**
 * @author nplekhanov
 */
public class Parser {
    public static Collection<Offer> parse(String html, String mark, Date now) {
        Document document = Jsoup.parse(html);
        Elements rows = document.select("table.list tbody tr");
        Collection<Offer> offers = new ArrayList<Offer>();
        for (Element row: rows.subList(1, rows.size())) {
            Elements cells = row.select("td");
            Offer offer = new Offer();
            offer.setMark(mark);
            offer.setModel(cells.get(0).text().trim());
            offer.setUrl(cells.get(0).select("a").attr("href").trim());
            offer.setPrice(Integer.parseInt(cells.get(1).text().replace(" ", "")));
            offer.setYear(Integer.parseInt(cells.get(2).text().replace(" ", "")));
            offer.setEngine(cells.get(3).text().trim());
            offer.setType(cells.get(4).text().trim());
            offer.setRightHand(!cells.get(4).select("img[alt=Правый руль]").isEmpty());
            offer.setRunning(Integer.parseInt(cells.get(5).text().replace(" ", "")));
            offer.setPhoto(!cells.get(6).select("img").isEmpty());
            offer.setColor(cells.get(8).select("div").get(0).attr("title").trim());
            offer.setCity(cells.get(9).text().trim());
            offer.setAvailability(cells.get(11).text().trim());
            offer.setParsedAt(now);
            offers.add(offer);
        }

        return offers;
    }

    public static void main(String[] args) throws IOException {

        for (Offer offer: parse(new String(FileCopyUtils.copyToByteArray(new File("D:/dev/cars2/toyota.htm"))), "Toyota", new Date())) {
            System.out.println(offer);
        }
    }
}
