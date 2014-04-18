<%@ page import="net.kkolyan.pivot.Pivot" %>
<%@ page import="org.springframework.jdbc.core.JdbcTemplate" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate" %>
<%@ page import="org.springframework.util.LinkedCaseInsensitiveMap" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="net.kkolyan.pivot.MapListH2WrappingExtractor" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return value;
    }
%>
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
            <input name="keyword" value="<%=getParameter(request, "keyword","")%>"/>
        </label>
        <label>
            Год выпуска от
            <input name="minYear" value="<%=getParameter(request, "minYear","1900")%>"/>
        </label>
        <label>
            Цена от
            <input name="minPrice" value="<%=getParameter(request, "minPrice","0")%>"/>
        </label>
        <label>
            до
            <input name="maxPrice" value="<%=getParameter(request, "maxPrice","1000000000")%>"/>
        </label>
        <label>
            Кол-во предложений от
            <input name="minCount" value="<%=getParameter(request, "minCount","0")%>"/>
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
                             "select mark, model, year, price, running " +
                             "from offers " +
                             "where (" + keywordMatching + ") " +
                             "and year >= :minYear ", params, new MapListH2WrappingExtractor("x", "" +
                     "select mark, model, year, " +
                     "cast(min(price) as int) prc_min, " +
                     "cast(max(price) as int) prc_max, " +
                     "cast(median(price) as int) prc_med, " +
                     "cast(min(running) as int) run_min, " +
                     "cast(max(running) as int) run_max, " +
                     "cast(median(running) as int) run_med, " +
                     "count(*) cnt " +
                     "from x " +
                     "group by mark, model, year " +
                     "having cnt >= :minCount " +
                     "and prc_med >= :minPrice and prc_med <= :maxPrice " +
                     "order by mark, model", params));

             Pivot pivot = new Pivot();
             pivot.setData(data);
             pivot.setXAxis("year");
             pivot.setYAxis("mark, model");
             pivot.setZAxis("prc_med, prc_min, prc_max, run_med, run_min, run_max, cnt");
             pivot.setZFormat("<b>%,d</b> (%,d - %,d) руб <br/> <b>%,d</b> (%,d - %,d) км <br/> <b>%s</b> шт");
//             pivot.setZFormat("~%,.0fр, Δ%,.0fр, ~%,.0fкм, Δ%,.0fкм, %sшт");
             request.setAttribute("pivot", pivot);

             pageContext.include("generic_pivot.jsp");
         } catch (Exception e) {
             e.printStackTrace(response.getWriter());
         }
     }

 %>
</body>
</html>
