package net.kkolyan.pivot.net.kkolyan.cars2.autoru;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import static org.apache.commons.lang.StringUtils.*;

/**
 * @author nplekhanov
 */
public class OffersImportTask {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // (Cron format: seconds, minutes, hours, day of month, month, day of week)
    //0 45 5 * * MON-FRI
//    @Scheduled(cron = "0 0 10 * * *")
    public void doImport() {
        doImport(new PrintWriter(System.out, true), 1000);
    }

    public void doImport(PrintWriter out, long interval) {

        Map<String,String> marks = new HashMap<String, String>();
        marks.put("AC", "1");  marks.put("Acura", "5");  marks.put("Alfa Romeo", "7");  marks.put("Aro", "11");
        marks.put("Asia", "12");  marks.put("Aston Martin", "14");  marks.put("Audi", "15");  marks.put("Austin", "16");  marks.put("Beijing", "23");  marks.put("Bentley", "25");  marks.put("BMW", "30");
        marks.put("BMW Alpina", "8");  marks.put("Brilliance", "671");  marks.put("Bugatti", "36");  marks.put("Buick", "37");  marks.put("BYD", "40");  marks.put("Byvin", "2838");
        marks.put("Cadillac", "41");  marks.put("Changan", "1284");  marks.put("ChangFeng", "646");  marks.put("Chery", "48");  marks.put("Chevrolet", "49");  marks.put("Chrysler", "50");
        marks.put("Citroen", "52");  marks.put("Dacia", "59");  marks.put("Dadi", "60");  marks.put("Daewoo", "61");  marks.put("Daihatsu", "63");  marks.put("Daimler", "64");
        marks.put("De Lorean", "67");  marks.put("Derways", "69");  marks.put("Dodge", "71");  marks.put("DongFeng", "597");  marks.put("Doninvest", "72");  marks.put("Eagle", "74");
        marks.put("FAW", "77");  marks.put("Ferrari", "79");  marks.put("Fiat", "80");  marks.put("Fisker", "2548");  marks.put("Ford", "82");  marks.put("Foton", "588");  marks.put("FSO", "87");
        marks.put("Geely", "88");  marks.put("Geo", "89");  marks.put("GMC", "90");  marks.put("Great Wall", "95");  marks.put("Hafei", "573");  marks.put("Haima", "2040");  marks.put("Honda", "104");
        marks.put("HuangHai", "847");  marks.put("Hummer", "107");  marks.put("Hurtan", "1245");  marks.put("Hyundai", "109");  marks.put("Infiniti", "114");
        marks.put("Iran Khodro", "112");  marks.put("Isuzu", "121");  marks.put("JAC", "124");  marks.put("Jaguar", "125");  marks.put("Jeep", "127");  marks.put("JMC", "1157");  marks.put("Kia", "134");
        marks.put("Koenigsegg", "137");  marks.put("KTM", "447");  marks.put("Lamborghini", "145");  marks.put("Lancia", "146");  marks.put("Land Rover", "147");
        marks.put("Landwind", "148");  marks.put("Lexus", "152");  marks.put("Lifan", "645");  marks.put("Lincoln", "153");  marks.put("Lotus", "154");  marks.put("Luxgen", "2974");
        marks.put("Mahindra", "160");  marks.put("Maserati", "164");  marks.put("Maybach", "165");  marks.put("Mazda", "166");  marks.put("Mc Laren", "167");
        marks.put("Mercedes-Benz", "170");  marks.put("Mercury", "171");  marks.put("Metrocab", "173");  marks.put("MG", "174");  marks.put("Mini", "177");  marks.put("Mitsubishi", "181");
        marks.put("Mitsuoka", "182");  marks.put("Morgan", "185");  marks.put("Nissan", "191");  marks.put("Oldsmobile", "196");  marks.put("Opel", "197");  marks.put("Pagani", "200");
        marks.put("Peugeot", "205");  marks.put("Plymouth", "206");  marks.put("Pontiac", "207");  marks.put("Porsche", "208");  marks.put("Proton", "211");  marks.put("PUCH", "212");
        marks.put("Renault", "217");  marks.put("Rolls-Royce", "219");  marks.put("Rover", "221");  marks.put("Saab", "222");  marks.put("Saturn", "226");  marks.put("Scion", "230");
        marks.put("SEAT", "231");  marks.put("ShuangHuan", "670");  marks.put("Skoda", "236");  marks.put("SMA", "784");  marks.put("Smart", "237");  marks.put("Spyker", "242");
        marks.put("Ssang Yong", "243");  marks.put("Subaru", "246");  marks.put("Suzuki", "247");  marks.put("TATA", "251");  marks.put("Tesla", "1689");  marks.put("Tianma", "254");
        marks.put("Tianye", "255");  marks.put("Toyota", "260");  marks.put("Trabant", "261");  marks.put("Volkswagen", "273");  marks.put("Volvo", "274");  marks.put("Vortex", "534");
        marks.put("Wartburg", "276");  marks.put("Westfield", "278");  marks.put("Wiesmann", "280");  marks.put("Xin Kai", "282");  marks.put("ZX", "780");  marks.put("Бронто", "2392");
        marks.put("ВАЗ", "288");  marks.put("ГАЗ", "292");  marks.put("ЗАЗ", "296");  marks.put("ЗИЛ", "297");  marks.put("ИЖ", "299");  marks.put("КамАЗ", "301");  marks.put("ЛУАЗ", "311");
        marks.put("Москвич (АЗЛК)", "316");  marks.put("СеАЗ", "232");  marks.put("СМЗ", "895");  marks.put("ТагАЗ", "1038");  marks.put("УАЗ", "336");  marks.put("Эксклюзив", "1670");

        Set<String> existingOffers = new HashSet<String>(fetchExistingOfferUrls());

        Date now = new Date();

        out.println("offers import started at "+now);

        int n = 0;
        int skipped = 0;

        Random random = new Random();

        for (Map.Entry<String, String> mark: marks.entrySet()) {

            out.println("parsing mark "+mark.getKey());

            for (int i = 0;; i ++) {

                if (interval > 0) {
                    try {
                        long shiftedInterval = (long) ((1 + random.nextDouble() * 0.25) * interval);
                        Thread.sleep(shiftedInterval);
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                }

                out.println("parsing page "+(i+1));

                String url = getUrl(mark.getValue(), i);

                Collection<Map<String, Object>> offers = getOffers(url, mark.getKey(), now);
                if (offers.isEmpty()) {
                    break;
                }
                for (Map<String,Object> offer: offers) {
                    String offerUrl = (String) offer.get("url");
                    if (!existingOffers.contains(offerUrl)) {
                        saveOffer(offer);
                        n ++;
                    } else {
                        skipped ++;
                    }
                }
                out.println(n+" offers parsed, "+ skipped +" skipped");
            }
        }
        out.println("offers import finished at "+new Date());
    }

    private String getUrl(String markId, int pageIndex) {
        Map<String,String> defaults = new HashMap<String, String>();

        // Легковые автомобили
        defaults.put("category_id", "15");
        // поддержаные
        defaults.put("section_id", "1");  defaults.put("subscribe_id", "");  defaults.put("filter_id", "");  defaults.put("submit", "Найти");
        defaults.put("mark_id", "");  defaults.put("year[1]", "");  defaults.put("year[2]", "");  defaults.put("color_id", "");  defaults.put("price_usd[1]", "");  defaults.put("price_usd[2]", "");
        defaults.put("currency_key", "RUR");  defaults.put("body_key", "");  defaults.put("run[1]", "");  defaults.put("run[2]", "");  defaults.put("engine_key", "0");
        defaults.put("engine_volume[1]", "");  defaults.put("engine_volume[2]", "");  defaults.put("drive_key", "");  defaults.put("engine_power[1]", "");
        defaults.put("engine_power[2]", "");  defaults.put("transmission_key", "0");  defaults.put("used_key", "");  defaults.put("wheel_key", "");  defaults.put("custom_key", "");
        defaults.put("available_key", "");  defaults.put("change_key", "");  defaults.put("owner_pts", "");  defaults.put("stime", "0");  defaults.put("country_id", "1");  defaults.put("has_photo", "0");
        defaults.put("region[]", "89");  defaults.put("region_id", "89");  defaults.put("sort_by", "2");  defaults.put("city_id", "");  defaults.put("output_format", "1");  defaults.put("client_id", "0");
        defaults.put("extras[1]", "0");  defaults.put("extras[2]", "0");  defaults.put("extras[3]", "0");  defaults.put("extras[4]", "0");  defaults.put("extras[5]", "0");  defaults.put("extras[6]", "0");
        defaults.put("extras[7]", "");  defaults.put("extras[8]", "0");  defaults.put("extras[9]", "0");  defaults.put("extras[10]", "0");  defaults.put("extras[11]", "0");  defaults.put("extras[12]", "");
        defaults.put("extras[13]", "0");  defaults.put("extras[14]", "0");  defaults.put("extras[15]", "0");  defaults.put("extras[16]", "0");  defaults.put("extras[17]", "0");
        defaults.put("extras[18]", "");  defaults.put("extras[19]", "");  defaults.put("extras[20]", "");  defaults.put("extras[21]", "");  defaults.put("extras[22]", "");  defaults.put("extras[23]", "0");
        defaults.put("extras[24]", "0");  defaults.put("extras[25]", "");  defaults.put("extras[26]", "");  defaults.put("extras[27]", "0");  defaults.put("extras[28]", "0");
        defaults.put("extras[29]", "");

        Map<String, String> params = new HashMap<String, String>(defaults);

        params.put("mark_id", markId);
        params.put("_p", String.valueOf(pageIndex + 1));
        params.put("used_key", "5");
        params.put("custom_key", "1");
        params.put("available_key", "1");

        Collection<String> encodedParams = new ArrayList<String>();
        for (Map.Entry<String, String> entry: params.entrySet()) {
            try {
                String key = URLEncoder.encode(entry.getKey(), "utf8");
                String value = URLEncoder.encode(entry.getValue(), "utf8");
                encodedParams.add(key + "=" + value);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }

        return  "http://cars.auto.ru/list/?"+ join(encodedParams, "&");
    }

    protected List<String> fetchExistingOfferUrls() {
        return jdbcTemplate.queryForList("select distinct url from offers", String.class);
    }

    protected void saveOffer(Map<String, Object> offer) {

        NamedParameterJdbcOperations operations = new NamedParameterJdbcTemplate(jdbcTemplate);

        Collection<String> columns = new ArrayList<String>(offer.keySet());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
        insert.setTableName("offers");
        insert.execute(offer);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Collection<Map<String,Object>> getOffers(String url, String mark, Date now) {
        for (int i = 0; i < 100; i ++) {
            Document document;
            try {
                document = Jsoup.parse(new URL(url), 3000);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            return Parser.parse(document, mark, now);
        }
        throw new IllegalStateException("can't access "+url);
    }
}
