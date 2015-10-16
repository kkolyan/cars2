package com.nplekhanov.cars2.v2.moto;

import com.nplekhanov.cars2.v2.Mark;
import com.nplekhanov.cars2.v2.ParsingStrategy;
import com.nplekhanov.cars2.v2.ShortDescription;
import com.nplekhanov.cars2.v2.auto2.SummaryItem;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * @author nplekhanov
 */
public class MotoParsingStrategy implements ParsingStrategy<Mark> {

    @Override
    public Collection<Mark> getParams() {
        return MotoMarks.getMarks();
    }

    @Override
    public String createUrl(Mark param, int page) {
        String url = "http://moto.auto.ru/list/" +
                "?category_id=1" +
                "&section_id=1" +
                "&subscribe_id=" +
                "&filter_id=" +
                "&mark_id=" + param.getMarkId() +
                "&year%5B1%5D=" +
                "&year%5B2%5D=" +
                "&color_id=" +
                "&price_usd%5B1%5D=" +
                "&price_usd%5B2%5D=" +
                "&currency_key=RUR" +
                "&type_id=" +
                "&run%5B1%5D=" +
                "&run%5B2%5D=" +
                "&cylinders=" +
                "&engine_volume%5B1%5D=" +
                "&engine_volume%5B2%5D=" +
                "&cylinders_type=" +
                "&engine_key=" +
                "&strokes=" +
                "&engine_power%5B1%5D=" +
                "&engine_power%5B2%5D=" +
                "&drive_key=" +
                "&used_key=" +
                "&transmission_key=" +
                "&haggle_key=" +
                "&custom_key=" +
                "&available_key=" +
                "&change_key=" +
                "&stime=0" +
                "&country_id=1" +
                "&has_photo=0" +
                "&region%5B%5D=89" +
                "&region%5B%5D=32" +
                "&region_id=89" +
                "&sort_by=2" +
                "&output_format=1" +
                "&client_id=0" +
                "&extras%5B1%5D=0" +
                "&extras%5B2%5D=0" +
                "&submit=%D0%9D%D0%B0%D0%B9%D1%82%D0%B8";
        if (page > 1) {
            url = url + "&_p="+page;
        }
        return url;
//        if (page > 2) {
//            return "file:samples/Мотоциклы_Подержанные_Kawasaki_Результаты_поиска_пусто.html";
//        }
//        return "file:samples/Мотоциклы_Подержанные_Kawasaki_Результаты_поиска.html";
    }

    @Override
    public Collection<? extends ShortDescription> parse(Document html, Mark requestedMark) {
        Collection<Offer> offers = new ArrayList<>();
        for (Element tr: html.select("#cars_sale > table > tbody > tr")) {
            if (tr.hasClass("header")) {
                continue;
            }
            Offer offer = new Offer();
            try {
                offer.setSourceHtml(tr.toString());
                offer.setMark(requestedMark.getMarkTitle());
                offer.setTitle(tr.select("td:nth-child(2) > a").first().ownText());
                offer.setUrl(tr.select("td:nth-child(2) > a").first().attr("href"));
                offer.setType(tr.select("td:nth-child(1) > img").first().attr("alt"));
                offer.setPrice(asInt(tr.select("td:nth-child(3) > nobr").first().ownText()));
                offer.setImagePresent(!tr.select("td:nth-child(4) > img").isEmpty());
                offer.setYear(asInt(tr.select("td:nth-child(5)").first().ownText()));
                offer.setRun(asInt(tr.select("td:nth-child(6) > nobr").first().ownText()));
                offer.setColor(tr.select("td:nth-child(7) > div").first().attr("title"));
                offer.setRegion(tr.select("td:nth-child(8)").first().ownText());
                offer.setCustomsPassed(tr.select("td:nth-child(9) > img").isEmpty());
                offer.setAvailable(tr.select("#cars_sale > table > tbody > tr:nth-child(2) > td:nth-child(10)").text().equals("Есть"));
            } catch (Exception e) {
                System.out.println(e);
                offer.setException(ExceptionUtils.getFullStackTrace(e));
            }
            offers.add(offer);
        }
        return offers;
    }

    private static int asInt(String text) {
        String s = text.replaceAll("[^0-9]", "");
        if (s.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(s);
    }

    @Override
    public String getReferer() {
        return "http://moto.auto.ru/extsearch/motorcycle/used/";
    }

    @Override
    public long getPauseMillis() {
        return 2500+new Random().nextInt(5000);
    }

    @Override
    public int getEntriesPerPageThreshold() {
        return 50;
    }
}
