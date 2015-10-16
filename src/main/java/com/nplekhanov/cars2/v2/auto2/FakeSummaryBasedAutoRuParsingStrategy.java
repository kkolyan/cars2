package com.nplekhanov.cars2.v2.auto2;

import com.nplekhanov.cars2.v2.ShortDescription;
import org.jsoup.nodes.Document;

import java.util.Collection;

/**
 * @author nplekhanov
 */
public class FakeSummaryBasedAutoRuParsingStrategy extends SummaryBasedAutoRuParsingStrategy {
    @Override
    public String getUsedMarkSummaryUrl(SummaryItem mark) {
        if (mark.getTitle().equals("Skoda")) {
            return "file:./samples/auto.ru.used_mark_summary.html";
        }
        return "file:./samples/auto.ru.used_mark_summary.empty.html";
    }

    @Override
    public long getPauseMillis() {
        return 0;
    }

    @Override
    public String createUrl(SummaryItem model, int page) {
        if (page > 1 || !model.getTitle().equals("Octavia")) {
            return "file:./samples/auto.ru.used_model.empty.html";
        }
        return "file:./samples/auto.ru.used_model.html";
    }

    @Override
    public String getUsedSummaryUrl() {
        return "file:./samples/auto.ru.used_summary.html";
    }
}
