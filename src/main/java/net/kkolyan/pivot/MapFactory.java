package net.kkolyan.pivot;

import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class MapFactory<V> {
    private String[] keys;

    public MapFactory(String... keys) {
        this.keys = keys;
    }

    public Map<String,V> create(V... values) {
        Map<String, V> map = new LinkedCaseInsensitiveMap<V>();
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            map.put(key, values[i]);
        }
        return map;
    }
}
