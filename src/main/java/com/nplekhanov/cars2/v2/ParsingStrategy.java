package com.nplekhanov.cars2.v2;

import com.nplekhanov.cars2.v2.auto2.SummaryItem;
import org.jsoup.nodes.Document;

import java.util.Collection;

/**
 * @author nplekhanov
 */
public interface ParsingStrategy <T extends MeaningfulTextProvider> {
    Collection<T> getParams();
    String createUrl(T param, int page);
    Collection<? extends ShortDescription> parse(Document html, T param);
    String getReferer();

    long getPauseMillis();

    int getEntriesPerPageThreshold();
}
