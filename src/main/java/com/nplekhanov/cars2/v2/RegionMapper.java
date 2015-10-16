package com.nplekhanov.cars2.v2;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author nplekhanov
 */
public class RegionMapper implements RowMapper<Region> {
    @Override
    public Region mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Region(rs.getLong(1), rs.getString(2), rs.getLong(3));
    }
}
