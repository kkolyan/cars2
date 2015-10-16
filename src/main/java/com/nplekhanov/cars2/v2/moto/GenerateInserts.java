package com.nplekhanov.cars2.v2.moto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nplekhanov.cars2.v2.SqlGen;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
* @author nplekhanov
*/
public class GenerateInserts {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        for (String line: FileUtils.readLines(new File("downloaded/moto.auto.ru.28.09.2015.txt"))) {
            Offer offer = mapper.readValue(line, Offer.class);
            System.out.println(SqlGen.generate(offer, "sourceHtml", "exception")+";");
        }
    }
}
