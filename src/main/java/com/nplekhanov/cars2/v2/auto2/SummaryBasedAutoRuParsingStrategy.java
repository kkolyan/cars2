package com.nplekhanov.cars2.v2.auto2;

import com.nplekhanov.cars2.v2.*;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

/**
 * @author nplekhanov
 */
public class SummaryBasedAutoRuParsingStrategy implements SummaryBasedParsingStrategy {

    @Override
    public String getUsedSummaryUrl() {
        return "http://auto.ru/cars/used/";
    }

    @Override
    public String createUrl(SummaryItem model, int page) {
        String url = "http://auto.ru" + model.getUrl();
        if (page > 1) {
            url += "?p="+page;
        }
        return url;
    }

    @Override
    public Collection<? extends ShortDescription> parse(Document html, SummaryItem mark, SummaryItem model) {
        return AutoRuShortDescription.parse(html, mark.getTitle());
    }

    @Override
    public long getPauseMillis() {
        Random random = new Random();
        if (random.nextInt(1000) < 2) {
            return 2000+random.nextInt(50000);
        }
        return 2000+ random.nextInt(5000);
    }

    @Override
    public int getEntriesPerPageThreshold() {
        return 36;
    }

    @Override
    public String getUsedMarkSummaryUrl(SummaryItem mark) {
        String url = mark.getUrl();
        if (url.startsWith("//")) {
            return "http:"+url;
        }
        return url;
    }
}
