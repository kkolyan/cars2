package com.nplekhanov.cars2.v2.auto2;

import com.nplekhanov.cars2.v2.Mark;
import com.nplekhanov.cars2.v2.ParsingStrategy;
import com.nplekhanov.cars2.v2.Paths;
import com.nplekhanov.cars2.v2.ShortDescription;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

/**
 * @author nplekhanov
 */
public class FakeAutoRuParsingStrategy extends AutoRuParsingStrategy {

    @Override
    public Collection<AutoParam> getParams() {
        Collection<AutoParam> params = new ArrayList<>();
        for (Map.Entry<String, String> entry: Marks.getMarks().entrySet()) {
            if (!entry.getValue().startsWith("Skoda")) {
                continue;
            }
            for (int i = 0; i < 20; i ++) {
                params.add(new AutoParam(new Mark(entry.getKey(), entry.getValue()), 2015 - i));
            }
        }
        return params;
    }

    @Override
    public String createUrl(AutoParam param, int page) {
        Mark mark = param.getMark();
        String path = Paths.getPaths().get(mark.getMarkTitle());
        if (path == null) {
            System.out.println("path for mark not found: "+mark.getMarkTitle());
        }
        if (page > 2 || !mark.getMarkTitle().equals("Skoda") || param.getYear() != 2015) {
            return "file:./samples/Купить б_у Skoda Octavia с пробегом_ продажа подержанных автомобилей Шкода Октавия, частные объявления с фото и ценами — АВТО.РУ.empty.html";
        }
        return "file:./samples/Купить б_у Skoda Octavia с пробегом_ продажа подержанных автомобилей Шкода Октавия, частные объявления с фото и ценами — АВТО.РУ.html";
    }

    @Override
    public long getPauseMillis() {
        return 0;
    }

}
