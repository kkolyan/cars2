package net.kkolyan.pivot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author nplekhanov
 */
public class Aggregators {
    public static final Aggregator AVG = new Aggregator() {
        @Override
        public Object aggregate(Collection<?> values) {
            double sum = 0;
            for (Object number: values) {
                sum += ((Number) number).doubleValue();
            }
            return sum / values.size();
        }
    };

    public static final Aggregator MEDIAN = new Aggregator() {
        @Override
        public Object aggregate(Collection<?> values) {
            List<Comparable> list = new ArrayList<Comparable>((Collection) values);
            Collections.sort(list);
            if (list.size() % 2 == 0) {
                Number a = (Number) list.get(list.size() / 2 - 1);
                Number b = (Number) list.get(list.size() / 2);
                return (a.doubleValue() + b.doubleValue()) / 2;
            }
            return list.get(list.size() / 2);
        }
    };

    public static final Aggregator COUNT_ALL = new Aggregator() {
        @Override
        public Object aggregate(Collection<?> values) {
            return values.size();
        }
    };

    public static void main(String[] args) {
        System.out.println(MEDIAN.aggregate(Arrays.asList(1,3,2)));
    }
}
