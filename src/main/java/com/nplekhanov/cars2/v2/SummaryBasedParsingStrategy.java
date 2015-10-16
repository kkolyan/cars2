package com.nplekhanov.cars2.v2;

import com.nplekhanov.cars2.v2.auto2.SummaryItem;
import org.jsoup.nodes.Document;

import java.util.Collection;

/**
 * @author nplekhanov
 */
public interface SummaryBasedParsingStrategy {
    String getUsedSummaryUrl();

    String createUrl(SummaryItem model, int page);
    Collection<? extends ShortDescription> parse(Document html, SummaryItem mark, SummaryItem model);
    String getUsedMarkSummaryUrl(SummaryItem mark);

    long getPauseMillis();

    int getEntriesPerPageThreshold();
}
