package com.nplekhanov.cars2.v2.auto2;

import com.nplekhanov.cars2.v2.SummaryBasedSearchRunner;

import java.io.IOException;

/**
 * @author nplekhanov
 */
public class SummaryBasedAutoParseRunner {
    public static void main(String[] args) throws IOException {
        SummaryBasedSearchRunner.parse(new SummaryBasedAutoRuParsingStrategy());
    }
}
