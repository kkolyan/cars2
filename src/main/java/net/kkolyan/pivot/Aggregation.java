package net.kkolyan.pivot;

/**
 * @author nplekhanov
 */
public class Aggregation {
    private String column;
    private String alias;
    private Aggregator aggregator;

    public Aggregation(String column, String alias, Aggregator aggregator) {
        this.column = column;
        this.alias = alias;
        this.aggregator = aggregator;
    }

    public String getColumn() {
        return column;
    }

    public String getAlias() {
        return alias;
    }

    public Aggregator getAggregator() {
        return aggregator;
    }
}
