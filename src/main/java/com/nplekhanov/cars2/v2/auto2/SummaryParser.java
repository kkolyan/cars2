package com.nplekhanov.cars2.v2.auto2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author nplekhanov
 */
public class SummaryParser {
    public static Collection<SummaryItem> parseUsedSummary(Document document) {
        Collection<SummaryItem> all = parseByCssQuery("body > div.content > div.main-page > div.main-page__promocar > div.main-page__rubricator > div > div > div.columns.columns_count_5.mmm-switcher__list.mmm-switcher__list_all li.mmm__item", document);
        Collection<SummaryItem> popular = parseByCssQuery("body > div.content > div.main-page > div.main-page__promocar > div.main-page__rubricator > div > div > div.columns.columns_count_5.mmm-switcher__list.mmm-switcher__list_popular li.mmm__item", document);
        Collection<String> popularTitles = new HashSet<>();
        for (SummaryItem item: popular) {
            popularTitles.add(item.getTitle());
        }
        Collection<SummaryItem> nonPopular = new ArrayList<>();
        for (SummaryItem item: all) {
            if (!popularTitles.contains(item.getTitle())) {
                nonPopular.add(item);
            }
        }
        return nonPopular;
    }

    private static Collection<SummaryItem> parseByCssQuery(String cssQuery, Document document) {Elements items = document.select(cssQuery);
        Collection<SummaryItem> summaries = new ArrayList<>();
        for (Element item: items) {
            String url = item.select("a").attr("href");
            String title = item.select("a").first().ownText();
            Element first = item.select("sup.mmm__count").first();
            int count;
            if (first != null && !first.ownText().isEmpty()) {
                count = Integer.parseInt(first.ownText());
            } else {
                count = 0;
            }
            summaries.add(new SummaryItem(url, title, count));
        }

        return summaries;
    }

    public static Collection<SummaryItem> parseUsedMarkSummary(Document document) {
        Elements items = document.select("#filter > div.fast-mmm > div:nth-child(3) li.fast-mmm-item");Collection<SummaryItem> summaries = new ArrayList<>();
        for (Element item: items) {
            String url = item.select("a").attr("href");
            String title = item.select("a").first().ownText();
            Element first = item.select("sup.fast-mmm-count").first();
            if (first != null) {
                int count = Integer.parseInt(first.ownText());
                summaries.add(new SummaryItem(url, title, count));
            }
        }
        return summaries;
    }

    public static class ParseUsedSummaryTest {

        public static void main(String[] args) throws IOException {
            Document document = Jsoup.parse(new File("samples/auto.ru.used_summary.html"), "utf8");
            Collection<SummaryItem> items = parseUsedSummary(document);
            for (SummaryItem item: items) {
                System.out.println(item);
            }

        }
    }

    public static class ParseUsedMarkSummaryTest {

        public static void main(String[] args) throws IOException {
            Document document = Jsoup.parse(new File("samples/auto.ru.used_mark_summary.html"), "utf8");
            Collection<SummaryItem> items = parseUsedMarkSummary(document);
            for (SummaryItem item: items) {
                System.out.println(item);
            }

        }
    }
}
