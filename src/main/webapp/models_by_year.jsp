<%@ page import="net.kkolyan.pivot.Pivot" %>
<%@ page import="org.springframework.jdbc.core.JdbcTemplate" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate" %>
<%@ page import="org.springframework.util.LinkedCaseInsensitiveMap" %>
<%@ page import="org.apache.commons.lang.ObjectUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="net.kkolyan.pivot.GroupUtil" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Collection" %>
<%@ page import="net.kkolyan.pivot.Aggregation" %>
<%@ page import="net.kkolyan.pivot.Aggregators" %>
<%@ page import="net.kkolyan.pivot.MapListH2WrappingExtractor" %>
<%@ page import="org.springframework.dao.DataAccessException" %>
<%@ page import="java.io.IOException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
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
        <input type="hidden" name="show" value="true"/>
    </form>
 <%
     response.flushBuffer();
     JdbcTemplate template = WebApplicationContextUtils.getRequiredWebApplicationContext(application).getBean(JdbcTemplate.class);

     NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(template);
     Map<String, Object> params = new LinkedCaseInsensitiveMap<Object>();
     for (Map.Entry<String, String[]> param: request.getParameterMap().entrySet()) {
         params.put(param.getKey(), param.getValue()[0]);
     }

     if (request.getParameter("show") != null) {

         List<String> likes = new ArrayList<String>();
         int i = 0;
         for (String keyword: params.get("keyword").toString().split("\\s")) {
             keyword = keyword.trim();
             if (keyword.isEmpty()) {
                 continue;
             }
             params.put("keyword"+i, keyword);
             likes.add("upper(concat(mark, ' ', model)) like upper(concat('%',:keyword" + i + ",'%'))");
             i ++;
         }
         String keywordMatching = "1=1";
         if (likes.size() > 0) {
             keywordMatching = StringUtils.join(likes, " or ");
         }
         params.remove("keyword");

         try {
             List<Map<String, Object>> data = namedTemplate.query(
                     "" +
                             "select mark, model, year, price " +
                             "from offers " +
                             "where (" + keywordMatching + ") " +
                             "and year > :minYear ", params, new MapListH2WrappingExtractor("x", "" +
                     "select mark, model, year, " +
                     "cast(median(price) as double) prc, " +
                     "count(*) cnt, " +
                     "cast((max(price) - min(price)) as double) as d " +
                     "from x " +
                     "group by mark, model, year " +
                     "having cnt > :minCount " +
                     "and prc > :minPrice and prc < :maxPrice " +
                     "order by mark, model", params));

             Pivot pivot = new Pivot();
             pivot.setData(data);
             pivot.setXAxis("year");
             pivot.setYAxis("mark, model");
             pivot.setZAxis("prc, d, cnt");
             pivot.setZFormat("~%,.0f, Δ%,.0f (%sшт)");
             request.setAttribute("pivot", pivot);

             pageContext.include("generic_pivot.jsp");
         } catch (Exception e) {
             e.printStackTrace(response.getWriter());
         }
     }

 %>
</body>
</html>
