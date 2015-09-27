package com.nplekhanov.cars2.v2;

import org.jsoup.nodes.Document;

import java.util.Collection;

/**
 * @author nplekhanov
 */
public interface ParsingStrategy {
    Collection<Mark> getMarks();
    String createUrl(Mark mark, int year, int page);
    Collection<? extends ShortDescription> parse(Document html, Mark requestedMark);
    String getReferer();
}
