<%@ page import="org.springframework.jdbc.core.JdbcTemplate" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="net.kkolyan.pivot.Pivot" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%!
    String str(Object o) {
        return o == null ? "" : o.toString();
    }
%>
<head>
    <title></title>
    <style>
        table {
            border-collapse: collapse;
        }
        td,th {
            border: 1px solid #CCC;
            padding: 1px 5px;
        }
        th {
            background: #EEE;
        }
    </style>
</head>
<body>
<%
    JdbcTemplate template = WebApplicationContextUtils.getRequiredWebApplicationContext(application).getBean(JdbcTemplate.class);

    String name = request.getParameter("NAME");
    String XAxis = request.getParameter("X");
    String YAxis = request.getParameter("Y");
    String ZAxis = request.getParameter("Z");
    String ZFormat = request.getParameter("ZFORMAT");
    String query = request.getParameter("QUERY");

    if (name == null) {
        name = "default";
    }
%>
<form>
    <input type="hidden" name="pivot" value="true"/>
    <label>
        X
        <input name="X" value="<%=str(XAxis)%>"/>
    </label>
    <label>
        Y
        <input name="Y" value="<%=str(YAxis)%>"/>
    </label>
    <label>
        Z
        <input name="Z" value="<%=str(ZAxis)%>"/>
    </label>
    <label>
        Z-format
        <input name="ZFORMAT" value="<%=str(ZFormat)%>"/>
    </label>
    <div>
    <label>
        Query
        <textarea name="QUERY" rows="7" cols="120"><%=str(query)%></textarea>
    </label>
    </div>
    <label>
        Name
        <input name="NAME" value="<%=name%>"/>
    </label>
    <input type="submit" value="Save and show"/>

    <%
        List<Map<String, Object>> queries = template.queryForList("select * from queries order by name");
        for (Map<String,?> row: queries) {
            List<String> param = new ArrayList<String>();
            for (Map.Entry<String, ?> entry: row.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    param.add(entry.getKey() +"="+ (URLEncoder.encode(value.toString(), "utf8")));
                }
            }
            %><a href="pivot.jsp?pivot=true&<%=StringUtils.join(param, "&")%>"><%=row.get("name")%></a> <%
        }
    %>
</form>

<%
    template.update(
            "merge into queries (name, query, x, y, z, ZFormat) key (name) values (?,?,?,?,?,?) ",
            name, query, XAxis, YAxis, ZAxis, ZFormat
    );

    String pivot = request.getParameter("pivot");
    if (pivot != null) {

        List<Map<String, Object>> data = template.queryForList(query);

        net.kkolyan.pivot.Pivot pivotInput = new net.kkolyan.pivot.Pivot();
        pivotInput.setData(data);
        pivotInput.setXAxis(XAxis);
        pivotInput.setYAxis(YAxis);
        pivotInput.setZAxis(ZAxis);
        pivotInput.setZFormat(ZFormat);

        request.setAttribute("pivot", pivotInput);

        request.getRequestDispatcher("generic_pivot.jsp").include(request, response);
    }

%>
</body>
</html>
