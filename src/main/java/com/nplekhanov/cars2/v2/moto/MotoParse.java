package com.nplekhanov.cars2.v2.moto;

import com.nplekhanov.cars2.v2.SearchRunner;

import java.io.IOException;

/**
 * @author nplekhanov
 */
public class MotoParse {
    public static void main(String[] args) throws IOException {
        SearchRunner.parse(new MotoParsingStrategy());
    }
}
