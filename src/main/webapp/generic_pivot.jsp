<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="net.kkolyan.pivot.Pivot" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!

    Object getValue(Map<String,?> row, String column, final String format) {
        if (!column.contains(",")) {
            return row.get(column);
        }
        Collection<Object> values = new ArrayList<Object>() {

            @Override
            public String toString() {
                if (format == null || format.isEmpty()) {
                    return StringUtils.join(this, " ");
                }
                return String.format(Locale.forLanguageTag("ru"), format, toArray());
            }
        };
        for (String key: column.split(",")) {
            key = key.trim();
            values.add(row.get(key));
        }
        return values;
    }

%>

<%
    Pivot pivot = (Pivot) request.getAttribute("pivot");

    if (pivot.getData().size() > 0) {
        Collection<Object> xValues;
        Collection<Object> yValues;
        Map<String,?> firstRow = pivot.getData().get(0);
//        if (getValue(firstRow, pivot.getYAxis(), null) instanceof Comparable) {
//            yValues = new TreeSet<Object>();
//        } else {
            yValues = new LinkedHashSet<Object>();
//        }
//        if (getValue(firstRow, pivot.getXAxis(), null) instanceof Comparable) {
//            xValues = new TreeSet<Object>();
//        } else {
            xValues = new LinkedHashSet<Object>();
//        }
        Map<List,Object> values = new HashMap<List, Object>();
        for (Map<String, Object> row: pivot.getData()) {
            Object x = getValue(row, pivot.getXAxis(), null);
            Object y = getValue(row, pivot.getYAxis(), pivot.getYFormat());
            xValues.add(x);
            yValues.add(y);
            Object z = getValue(row, pivot.getZAxis(), pivot.getZFormat());
            values.put(Arrays.asList(x, y), z);
        }
%>
<%
%><table><%
    int row = 0;
    for (Object y: yValues) {
        if (row ++ % 10 == 0) {
%><tr><%
%><th></th><%
    for (Object x: xValues) {
%><th><%=x%></th><%
    }
%></tr><%
    }
%><tr><%
%><th nowrap><%=y%></th><%
    for (Object x: xValues) {
        Object v = values.get(Arrays.asList(x, y));
%><td><%= v == null ? "" : v %></td><%
    }
%></tr><%
    }
%></table><%

        }

%>