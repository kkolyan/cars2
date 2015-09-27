package com.nplekhanov.cars2.v2.auto2;

import com.nplekhanov.cars2.v2.Mark;
import com.nplekhanov.cars2.v2.ParsingStrategy;
import com.nplekhanov.cars2.v2.Paths;
import com.nplekhanov.cars2.v2.ShortDescription;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class AutoRuParsingStrategy implements ParsingStrategy {
    @Override
    public Collection<Mark> getMarks() {
        Collection<Mark> marks = new ArrayList<>();
        for (Map.Entry<String, String> entry: Marks.getMarks().entrySet()) {
            marks.add(new Mark(entry.getKey(), entry.getValue()));
        }
        return marks;
    }

    @Override
    public String createUrl(Mark mark, int year, int page) {
        String path = Paths.getPaths().get(mark.getMarkTitle());
        if (path == null) {
            System.out.println("path for mark not found: "+mark.getMarkTitle());
        }
        String url = "" +
                "http://auto.ru" + path +
                "?search%5Bsection_id%5D=0" +
                "&search%5Bmark%5D%5B0%5D=" + mark.getMarkId() +
                "&search%5Bmark-folder%5D%5B0%5D="+mark.getMarkId()+"-0" +
                "&search%5Bsalon_id%5D=" +
                "&search%5Byear%5D%5Bmin%5D=" + year +
                "&search%5Byear%5D%5Bmax%5D=" + year +
                "&search%5Bprice%5D%5Bmin%5D=" +
                "&search%5Bprice%5D%5Bmax%5D=" +
                "&search%5Bengine_volume%5D%5Bmin%5D=" +
                "&search%5Bengine_volume%5D%5Bmax%5D=" +
                "&search%5Brun%5D%5Bmax%5D=" +
                "&search%5Bengine_power%5D%5Bmin%5D=" +
                "&search%5Bengine_power%5D%5Bmax%5D=" +
                "&search%5Bcustom%5D=1" +
                "&search%5Bacceleration%5D%5Bmin%5D=" +
                "&search%5Bacceleration%5D%5Bmax%5D=" +
                "&search%5Bstate%5D=1" +
                "&search%5Bgeo_region%5D=32%2C89" +
                "&search%5Bgeo_city%5D=" +
                "&search%5Bgeo_country%5D=" +
                "&search%5Bgeo_similar_cities%5D=" +
                "&search%5Bperiod%5D=0" +
                "&show_sales=1";
        if (page > 1) {
            url += "&p="+page;
        }
        return url;
    }

    @Override
    public Collection<? extends ShortDescription> parse(Document html, Mark requestedMark) {
        return AutoRuShortDescription.parse(html, requestedMark.getMarkTitle());
    }

    @Override
    public String getReferer() {
        return "http://auto.ru/cars/mercedes/all/?search%5Bsection_id%5D=0&search%5Bmark%5D%5B0%5D=170&search%5Bmark-folder%5D%5B0%5D=170-0&search%5Bsalon_id%5D=&search%5Byear%5D%5Bmin%5D=2015&search%5Byear%5D%5Bmax%5D=2015&search%5Bprice%5D%5Bmin%5D=&search%5Bprice%5D%5Bmax%5D=&search%5Bengine_volume%5D%5Bmin%5D=&search%5Bengine_volume%5D%5Bmax%5D=&search%5Brun%5D%5Bmax%5D=&search%5Bengine_power%5D%5Bmin%5D=&search%5Bengine_power%5D%5Bmax%5D=&search%5Bcustom%5D=1&search%5Bacceleration%5D%5Bmin%5D=&search%5Bacceleration%5D%5Bmax%5D=&search%5Bstate%5D=1&search%5Bgeo_region%5D=32%2C89&search%5Bgeo_city%5D=&search%5Bgeo_country%5D=&search%5Bgeo_similar_cities%5D=&search%5Bperiod%5D=0&show_sales=1&p=2";
    }
}
