package net.kkolyan.pivot;

import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class GroupUtil {
    public static List<Map<String,Object>> extractGrouped(List<Map<String,Object>> data, Collection<Aggregation> aggregators, Collection<String> groupColumns) {
        MultiValueMap<Map<String,?>,Map<String,?>> byGroups = new LinkedMultiValueMap<Map<String,?>, Map<String, ?>>();
        for (Map<String, ?> entry: data) {
            byGroups.add(getValues(entry, groupColumns), entry);
        }
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        for (Map.Entry<Map<String, ?>, List<Map<String, ?>>> entry: byGroups.entrySet()) {
            Map<String,Object> row = new LinkedCaseInsensitiveMap<Object>();
            row.putAll(entry.getKey());

            Map<String, List<Object>> valueMap = zipMap(entry.getValue());
            for (Aggregation aggregation: aggregators) {
                Collection<?> values = valueMap.get(aggregation.getColumn());
                Object value = aggregation.getAggregator().aggregate(values);
                row.put(aggregation.getAlias(), value);
            }
            results.add(row);
        }
        return results;

    }

    private static  <K,V> Map<K,V> getValues(Map<K,V> map, Collection<K> keys) {
        Map<K,V> result = new HashMap<K, V>();
        for (K key: keys) {
            result.put(key, map.get(key));
        }
        return result;
    }

    private static <K,V> Map<K,List<V>> zipMap(List<Map<K,? extends V>> maps) {
        MultiValueMap<K,V> result = new LinkedMultiValueMap<K, V>();
        for (Map<K, ? extends V> map: maps) {
            for (Map.Entry<K, ? extends V> entry: map.entrySet()) {
                result.add(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

}
