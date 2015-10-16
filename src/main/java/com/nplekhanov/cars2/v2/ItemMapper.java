package com.nplekhanov.cars2.v2;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author nplekhanov
 */
public class ItemMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Item(rs.getLong(1), rs.getString(2));
    }
}
