package com.nplekhanov.cars2.v2.moto;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nplekhanov.cars2.v2.SqlGen;
import org.apache.commons.io.FileUtils;
import org.h2.jdbc.JdbcConnection;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.io.File;
import java.io.IOException;
import java.security.Timestamp;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author nplekhanov
 */
public class MotoH2Processing {
    public static void main(String[] args) throws IOException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        JdbcTemplate template = new JdbcTemplate(new SingleConnectionDataSource(new JdbcConnection("jdbc:h2:file:results/moto/db", new Properties()), true));
        if (false) {
            template.execute("CREATE TABLE moto (\n" +
                    "  mark  VARCHAR(500),\n" +
                    "  title VARCHAR(500), \n" +
                    "  url VARCHAR(500), \n" +
                    "  type VARCHAR(500), \n" +
                    "  price NUMBER, \n" +
                    "  image_present BOOLEAN, \n" +
                    "  year NUMBER, \n" +
                    "  run NUMBER, \n" +
                    "  color VARCHAR(500), \n" +
                    "  region VARCHAR(500), \n" +
                    "  customs_passed BOOLEAN,\n" +
                    "  available BOOLEAN, \n" +
                    "  exception VARCHAR(500), \n" +
                    "  source_html VARCHAR(5000)\n" +
                    ")");
        }
        final List<Offer> offers = new ArrayList<>();
        for (String line: FileUtils.readLines(new File("results/1444807347935.txt"))) {
            Offer offer = mapper.readValue(line, Offer.class);
            offers.add(offer);
            System.out.println(SqlGen.generate(offer)+";");
        }
        final long time = System.currentTimeMillis();

        template.batchUpdate("\n" +
                "INSERT INTO moto (\n" +
                "  as_of,\n" +
                "  mark,\n" +
                "  title,\n" +
                "  url,\n" +
                "  type,\n" +
                "  price,\n" +
                "  image_present,\n" +
                "  year,\n" +
                "  run,\n" +
                "  color,\n" +
                "  region,\n" +
                "  customs_passed,\n" +
                "  available,\n" +
                "  exception,\n" +
                "  source_html)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Offer offer = offers.get(i);
                int n = 0;
                ps.setTimestamp(++n, new java.sql.Timestamp(time));
                ps.setString(++n, offer.getMark());
                ps.setString(++n, offer.getTitle());
                ps.setString(++n, offer.getUrl());
                ps.setString(++n, offer.getType());
                ps.setInt(++n, offer.getPrice());
                ps.setBoolean(++n, offer.isImagePresent());
                ps.setInt(++n, offer.getYear());
                ps.setInt(++n, offer.getRun());
                ps.setString(++n, offer.getColor());
                ps.setString(++n, offer.getRegion());
                ps.setBoolean(++n, offer.isCustomsPassed());
                ps.setBoolean(++n, offer.isAvailable());
                ps.setString(++n, offer.getException());
                ps.setString(++n, offer.getSourceHtml());
            }

            @Override
            public int getBatchSize() {
                return offers.size();
            }
        });
    }
}
