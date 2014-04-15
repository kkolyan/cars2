<%@ page import="net.kkolyan.pivot.Pivot" %>
<%@ page import="org.springframework.jdbc.core.JdbcTemplate" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate" %>
<%@ page import="org.springframework.util.LinkedCaseInsensitiveMap" %>
<%@ page import="org.apache.commons.lang.ObjectUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
    <form>
        <label>
            Название содержит
            <input name="keyword" value="<%=ObjectUtils.toString(request.getParameter("keyword"), "")%>"/>
        </label>
        <label>
            Год выпуска от
            <input name="minYear" value="<%=ObjectUtils.toString(request.getParameter("minYear"), "1900")%>"/>
        </label>
        <label>
            Цена от
            <input name="minPrice" value="<%=ObjectUtils.toString(request.getParameter("minPrice"), "0")%>"/>
        </label>
        <label>
            до
            <input name="maxPrice" value="<%=ObjectUtils.toString(request.getParameter("maxPrice"), "1000000000")%>"/>
        </label>
        <label>
            Кол-во предложений от
            <input name="minCount" value="<%=ObjectUtils.toString(request.getParameter("minCount"), "0")%>"/>
        </label>
        <input type="submit" value="Показать"/>
    </form>
 <%
     JdbcTemplate template = WebApplicationContextUtils.getRequiredWebApplicationContext(application).getBean(JdbcTemplate.class);

     NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(template);
     Map<String, Object> params = new LinkedCaseInsensitiveMap<Object>();
     for (Map.Entry<String, String[]> param: request.getParameterMap().entrySet()) {
         params.put(param.getKey(), param.getValue()[0]);
     }

     if (request.getParameter("submit") != null) {

         List<Map<String, Object>> data = namedTemplate.queryForList(
                 "" +
                         "select mark, model, year, avg(price) prc, count(*) cnt " +
                         "where mark + ' ' + model like '%'+:keyword+'%' " +
                         "and year > :minYear " +
                         "and prc > :minPrice and prc < :maxPrice " +
                         "from offers group by mark, model, year " +
                         "having cnt > :minCount " +
                         "order by mark, model", params);
         net.kkolyan.pivot.Pivot pivot = new net.kkolyan.pivot.Pivot();
         pivot.setData(data);
         pivot.setXAxis("year");
         pivot.setYAxis("mark, model");
         pivot.setZAxis("prc, cnt");
         pivot.setZFormat("~%,d р (%sшт)");
         request.setAttribute("pivot", pivot);

         request.getRequestDispatcher("generic_pivot.jsp").include(request, response);
     }

 %>
</body>
</html>
