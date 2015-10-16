package com.nplekhanov.cars2.v2.auto2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.nplekhanov.cars2.v2.Lambda;
import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author nplekhanov
 */
public class Processing {

    private static ApplicationContext applicationContext;
    private static JdbcTemplate jdbcTemplate;

    static long startedAt;
    static int linesDone;
    static long total;

    static {
        applicationContext = new ClassPathXmlApplicationContext("processing.xml");
        jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
    }

    public static void main(String[] args) throws IOException {
        total = 0;
        for (File dir: new File("dumps/verified").listFiles()) {
            Scanner scanner = new Scanner(new File(dir, "parsed.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                total ++;
            }
        }

        System.out.println(new Date()+" total: "+total);

        startedAt = System.currentTimeMillis();

        linesDone = 0;
        Collection<String> buffer = new ArrayList<>();
        for (File dir: new File("dumps/verified").listFiles()) {
            try (Scanner scanner = new Scanner(new File(dir, "parsed.txt"))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    buffer.add(line);
                    if (buffer.size() >= 10000) {
                        store(buffer);
                        buffer.clear();
                    }
                    linesDone++;
                }
                store(buffer);
            }
        }
    }

    private static void store(Collection<String> lines) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Collection<AutoRuShortDescription> downloaded = new ArrayList<>();

        for (String line: lines) {
            downloaded.add(mapper.readValue(line, AutoRuShortDescription.class));
        }

        for (AutoRuShortDescription d: downloaded) {
            if (d.getModel() == null && d.getMark() == null && d.getTitle() != null && d.lookupCardDetails() != null) {
//                for (String mark: Marks.getMarks().values()) {
//                    if (d.getTitle().startsWith(mark)) {
//                        d.setMark(mark);
//                        d.setModel(d.getTitle().substring(mark.length()).trim());
//                        break;
//                    }
//                }
                String title = d.getTitle();
                d.setModel(title.replace(d.getRequestedMark(), "").trim());
                d.setMark(d.getRequestedMark());

                if (d.getModel() == null) {
                    d.setMark(d.getRequestedMark());
                    d.setModel(d.lookupCardDetails().getCard_model());
                }
                if (d.getModel() == null) {
                    String mark = d.lookupCardDetails().getCard_mark();
                    if (d.getTitle().startsWith(mark)) {
                        d.setMark(mark);
                        d.setModel(d.getTitle().substring(mark.length()).trim());
                    }
                }
            }
        }
        System.out.println("downloaded " + downloaded.size());

        Collection<AutoRuShortDescription> failed = Lambda.filter(downloaded, new Predicate<AutoRuShortDescription>() {
            @Override
            public boolean apply(AutoRuShortDescription o) {
                return o.getException() != null;
            }
        });
        System.out.println("failed " + failed.size());

        Collection<AutoRuShortDescription> invalid = Lambda.filter(downloaded, new Predicate<AutoRuShortDescription>() {
            @Override
            public boolean apply(AutoRuShortDescription o) {
                return o.getException() == null && (o.getModel() == null || o.getMark() == null);
            }
        });
        System.out.println("invalid " + invalid.size());

        List<AutoRuShortDescription> succeed = Lambda.filter(downloaded, new Predicate<AutoRuShortDescription>() {
            @Override
            public boolean apply(AutoRuShortDescription o) {
                return o.getException() == null && !(o.getModel() == null || o.getMark() == null);
            }
        });
        for (AutoRuShortDescription d: succeed) {
            if (d.getMark().equals("Nissan") && d.getModel().trim().isEmpty()) {
                System.out.println(d);
            }
        }
        Function<AutoRuShortDescription, String> getMark = new Function<AutoRuShortDescription, String>() {
            @Override
            public String apply(AutoRuShortDescription input) {
                return input.getMark();
            }
        };
        Function<AutoRuShortDescription, String> getModel = new Function<AutoRuShortDescription, String>() {
            @Override
            public String apply(AutoRuShortDescription input) {
                return input.getModel();
            }
        };

        if (false)
        for (Map.Entry<String, Collection<AutoRuShortDescription>> entry: Lambda.groupBy(succeed, getMark).entrySet()) {
            System.out.println("Mark: " + entry.getKey() + " (" + entry.getValue().size() + ")");

            for (Map.Entry<String, Collection<AutoRuShortDescription>> modelEntry: Lambda.groupBy(entry.getValue(), getModel).entrySet()) {
                System.out.println("    " + modelEntry.getKey() + " (" + modelEntry.getValue().size() + ")");
            }
        }

        int i = 0;
        for (final List<AutoRuShortDescription> batch: Lists.partition(succeed, 10000)) {

            String q = "insert into offers (\n" +
                    "    mark,\n" +
                    "    model,\n" +
                    "    url,\n" +
                    "    price,\n" +
                    "    year,\n" +
                    "    engine,\n" +
                    "    subtitle,\n" +
                    "    type,\n" +
                    "    right_hand,\n" +
                    "    running,\n" +
                    "    photo,\n" +
                    "    color,\n" +
                    "    city,\n" +
                    "    availability,\n" +
                    "    parsed_at\n" +
                    ") values (\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    ?,\n" +
                    "    now()\n" +
                    "";
            BatchPreparedStatementSetter pss = new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    AutoRuShortDescription d = batch.get(i);
                    int n = 0;
                    ps.setString(++n, d.getMark());
                    ps.setString(++n, d.getModel());
                    ps.setString(++n, d.getUrl());
                    ps.setInt(++n, parseInt(d.getPrice()));
                    ps.setInt(++n, parseInt(d.getYear()));
                    ps.setObject(++n, d.getShortDetails());
                    ps.setObject(++n, d.getSubTitle());
                    ps.setObject(++n, d.getBodyType());
                    ps.setObject(++n, null);
                    ps.setInt(++n, parseInt(d.getRun()));
                    ps.setObject(++n, null);
                    ps.setString(++n, d.getColor());
                    ps.setString(++n, d.getRegion());
                    ps.setObject(++n, null);

                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            };
            jdbcTemplate.batchUpdate(q + ")", pss);
            i += batch.size();
            System.out.println(new Date()+" inserted "+i+" of "+succeed.size()+" offers");
        }

        double progress = 1.0 * linesDone / total;
        long eta = (long) ((1 - progress) * (System.currentTimeMillis() - startedAt) / progress);
        System.out.println(new Date()+" "+linesDone+" of "+ total+"("+String.format("%.02f", progress * 100.0)+")%, ETA: "+new Duration(eta).format());
    }

    private static int parseInt(String text) {
        if (text.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }
}
