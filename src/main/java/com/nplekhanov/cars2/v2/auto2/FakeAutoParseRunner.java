package com.nplekhanov.cars2.v2.auto2;

import com.nplekhanov.cars2.v2.SearchRunner;

import java.io.IOException;

/**
 * @author nplekhanov
 */
public class FakeAutoParseRunner {
    public static void main(String[] args) throws IOException {
        SearchRunner.parse(new FakeAutoRuParsingStrategy());
    }
}
