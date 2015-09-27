package com.nplekhanov.cars2.v2;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.nplekhanov.cars2.v2.auto2.Processing;

import java.util.*;

/**
 * @author nplekhanov
 */
public class Lambda {
    public static  <T> List<T> filter(Collection<? extends T> list, Predicate<T> filter) {
        List<T> filtered = new ArrayList<>();
        for (T t: list) {
            if (filter.apply(t)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    public static  <K,V> Map<K,Collection<V>> groupBy(Collection<? extends V> list, Function<V,K> mapper) {
        Multimap<K,V> map = createMultiMap();
        for (V v: list) {
            map.put(mapper.apply(v), v);
        }
        return map.asMap();
    }

    private static  <K,V> Multimap<K,V> createMultiMap() {
        Map<K, Collection<V>> map = new HashMap<>();
        Supplier<? extends List<V>> factory = new Supplier<List<V>>() {
            @Override
            public List<V> get() {
                return new ArrayList<>();
            }
        };
        return Multimaps.newListMultimap(map, factory);
    }
}
