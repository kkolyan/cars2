package net.kkolyan.pivot;

import org.apache.commons.lang.StringUtils;
import org.h2.jdbc.JdbcConnection;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
* @author nplekhanov
*/
public abstract class AbstractH2WrappingExtractor<T> implements org.springframework.jdbc.core.ResultSetExtractor<T> {
    private String tempTableName;

    AbstractH2WrappingExtractor(String tempTableName) {
        this.tempTableName = tempTableName;
    }

    @Override
    public T extractData(ResultSet rs) throws SQLException, DataAccessException {

        SingleConnectionDataSource ds = new SingleConnectionDataSource(new JdbcConnection("jdbc:h2:mem:", new Properties()), true);
        try {
            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(ds);

            ResultSetMetaData md = rs.getMetaData();

            List<String> colDefs = new ArrayList<String>();
            for (int i = 0; i < md.getColumnCount(); i ++) {
                colDefs.add(md.getColumnLabel(i + 1) + " " + md.getColumnTypeName(i + 1));
            }

            template.getJdbcOperations().execute("create local temporary table "+tempTableName+" (" + StringUtils.join(colDefs, ", ") + ")");
            template.getJdbcOperations().execute("create aggregate median for \"net.kkolyan.pivot.MedianAverageAggregationFunction\"");
            SimpleJdbcInsert insert = new SimpleJdbcInsert(ds);
            insert.setTableName(tempTableName);
            while (rs.next()) {
                Map<String,Object> row = new LinkedCaseInsensitiveMap<Object>();
                for (int i = 0; i < md.getColumnCount(); i ++) {
                    Object value = JdbcUtils.getResultSetValue(rs, i + 1);
                    row.put(md.getColumnLabel(i + 1), value);
                }
                insert.execute(row);
            }

            return extractData(template);

        } finally {
            ds.destroy();
        }
    }

    protected abstract T extractData(NamedParameterJdbcOperations operations);

    public static void main(String[] args) throws SQLException {
        SingleConnectionDataSource ds = new SingleConnectionDataSource(new JdbcConnection("jdbc:h2:mem:", new Properties()), true);

        JdbcTemplate template = new JdbcTemplate(ds);
        template.execute("create aggregate med for \"net.kkolyan.pivot.MedianAverageAggregationFunction\"");
        template.execute("create table x (a varchar, b number)");
        template.execute("insert into x values ('BMW', 16)");
        template.execute("insert into x values ('BMW', 12)");
        template.execute("insert into x values ('BMW', 10)");
        template.execute("insert into x values ('Audi', 12)");
        template.execute("insert into x values ('Audi', 13)");
        System.out.println(template.queryForList("select a, med(b) from x group by a"));

        System.out.println(template.queryForList("select cast(? as double) from dual", 12));
    }
}
