package net.kkolyan.pivot;

import org.h2.api.AggregateFunction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nplekhanov
 */
public class MedianAverageAggregationFunction implements AggregateFunction {
    private List<Object> values = new ArrayList<Object>();

    @Override
    public void init(Connection conn) throws SQLException {
    }

    @Override
    public int getType(int[] inputTypes) throws SQLException {
        return Types.DOUBLE;
    }

    @Override
    public void add(Object value) throws SQLException {
        values.add(value);
    }

    @Override
    public Object getResult() throws SQLException {
        return Aggregators.MEDIAN.aggregate(values);
    }
}
