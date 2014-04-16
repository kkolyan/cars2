package net.kkolyan.pivot;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author nplekhanov
 */
public class GroupUtilTest {
    @Test
    public void test1() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        MapFactory<Object> maps = new MapFactory<Object>("mark", "price", "year");
        data.add(maps.create("BMW", 100, 2001));
        data.add(maps.create("BMW", 70, 2002));
        data.add(maps.create("BMW", 80, 2003));
        data.add(maps.create("BMW", 50, 2003));
        data.add(maps.create("Audi", 110, 2004));
        data.add(maps.create("Audi", 140, 2005));
        data.add(maps.create("Skoda", 130, 2006));
        Collection<Aggregation> aggregators = new ArrayList<Aggregation>();
        aggregators.add(new Aggregation("price", "av.price", Aggregators.AVG));
        List<Map<String, Object>> grouped = GroupUtil.extractGrouped(data, aggregators, Arrays.asList("mark"));
        System.out.println(grouped);
        assertEquals(3, grouped.size());
        assertEquals("BMW", grouped.get(0).get("mark"));
        assertEquals(75.0, grouped.get(0).get("av.price"));
        assertEquals("Audi", grouped.get(1).get("mark"));
        assertEquals(125.0, grouped.get(1).get("av.price"));
        assertEquals("Skoda", grouped.get(2).get("mark"));
        assertEquals(130.0, grouped.get(2).get("av.price"));
    }
}
