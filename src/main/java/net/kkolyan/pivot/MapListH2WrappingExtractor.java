package net.kkolyan.pivot;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;
import java.util.Map;

/**
* @author nplekhanov
*/
public class MapListH2WrappingExtractor extends AbstractH2WrappingExtractor<List<Map<String,Object>>> {
    private final String query;
    private final Map<String, ?> args;

    public MapListH2WrappingExtractor(String tempTableName, String query, Map<String, ?> args) {
        super(tempTableName);
        this.query = query;
        this.args = args;
    }

    @Override
    protected List<Map<String, Object>> extractData(NamedParameterJdbcOperations operations) {
        return operations.queryForList(query, args);
    }
}
