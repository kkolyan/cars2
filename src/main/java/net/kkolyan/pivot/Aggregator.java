package net.kkolyan.pivot;

import java.util.Collection;

/**
* @author nplekhanov
*/
public interface Aggregator {
    Object aggregate(Collection<?> values);
}
