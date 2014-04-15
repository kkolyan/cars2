package net.kkolyan.pivot;

import java.util.List;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class Pivot {
    List<Map<String, Object>> data;
    String xAxis;
    String yAxis;
    String zAxis;
    String zFormat;

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public String getXAxis() {
        return xAxis;
    }

    public void setXAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getYAxis() {
        return yAxis;
    }

    public void setYAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    public String getZAxis() {
        return zAxis;
    }

    public void setZAxis(String zAxis) {
        this.zAxis = zAxis;
    }

    public String getZFormat() {
        return zFormat;
    }

    public void setZFormat(String zFormat) {
        this.zFormat = zFormat;
    }
}
