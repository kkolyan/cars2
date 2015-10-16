<%@ page import="net.kkolyan.pivot.Pivot" %>
<%@ page import="org.springframework.jdbc.core.JdbcTemplate" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate" %>
<%@ page import="org.springframework.util.LinkedCaseInsensitiveMap" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="net.kkolyan.pivot.MapListH2WrappingExtractor" %>
<%@ page import="net.kkolyan.pivot.KeywordExpressionParser" %>
<%@ page import="com.nplekhanov.cars2.v2.RegionMapper" %>
<%@ page import="com.nplekhanov.cars2.v2.Region" %>
<%@ page import="java.util.*" %>
<%@ page import="com.nplekhanov.cars2.v2.Item" %>
<%@ page import="com.nplekhanov.cars2.v2.ItemMapper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null || value.isEmpty()) {
            value = defaultValue;
        }
        value = value.replace("\"", "&quot;");
        return value;
    }
    Collection<Long> getLongParameters(HttpServletRequest request, String name) {
        String[] value = request.getParameterValues(name);
        Collection<Long> values = new LinkedHashSet<>();
        if (value != null) {
            for (String v: value) {
                values.add(Long.parseLong(v));
            }
        }
        return values;
    }
%><%
    boolean useType = getParameter(request, "use_type", "no").equals("yes");
    boolean useSubtitle = getParameter(request, "use_subtitle", "no").equals("yes");
    Collection<Long> regions = getLongParameters(request, "regions");

    JdbcTemplate template = WebApplicationContextUtils.getRequiredWebApplicationContext(application).getBean(JdbcTemplate.class);
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
            min-width: 240px;
        }
        th {
            background: #EEE;
        }

        .offer-field-type {
            color: #5a0a04;
        }
        .offer-field-subtitle {
            color: #2634a6;
        }
    </style>
</head>
<body>
    <form>
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
        <label>
            Предложения не старее N месяцев
            <input name="maxOfferAgeMonth" value="<%=getParameter(request, "maxOfferAgeMonth","3")%>"/>
        </label>
        <input type="submit" value="Показать"/>
        <input type="hidden" name="show" value="true"/>
        <br/>
        <label>
            Название содержит
            <input name="keyword" size="100" value="<%=getParameter(request, "keyword","")%>"/>
        </label>
        <label>
            <input name="use_subtitle" type="checkbox" <%= useSubtitle ? "checked" : "" %> value="yes"/>
            Учитывать комплектацию
        </label>
        <label>
            <input name="use_type" type="checkbox" <%= useType ? "checked" : "" %> value="yes"/>
            Учитывать тип кузова
        </label>
        <div style="width:100%; height: 100px; overflow-x: scroll;">
            <table>
            <%
                int rows = 4;
                List<Region> regionOffers = template.query("" +
                        "select c.id, c.name, count(*) cnt " +
                        "from city c inner join offers o " +
                        "on c.name = o.city " +
                        "group by c.id, c.name " +
                        "order by cnt desc", new RegionMapper());
                Region[][] regionGrid = new Region[rows][regionOffers.size() / rows + 1];
                for (int i = 0; i < regionOffers.size(); i ++) {
                    int row = i % rows;
                    int col = i / rows;
                    regionGrid[row][col] = regionOffers.get(i);
                }
                for (Region[] row: regionGrid) {
                    %> <tr> <%
                    for (Region city: row) {
                        if (city == null) {
                            continue;
                        }
                        %> <td style="border: none; margin: 0; padding: 0" nowrap >
                            <label title="<%=city.getName()%>" style=" overflow: hidden;">
                                <input name="regions" type="checkbox" <%= regions.contains(city.getId()) ? "checked" : "" %> value="<%=city.getId()%>"/>
                                    <%=city.getName()%> (<%=city.getOffers()%>)
                            </label></td> <%
                    }
                    %></tr> <%
                }
            %>

            </table>
        </div>
    </form>
 <%
     response.flushBuffer();

     NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(template);
     Map<String, Object> params = new LinkedCaseInsensitiveMap<Object>();
     for (Map.Entry<String, String[]> param: request.getParameterMap().entrySet()) {
         params.put(param.getKey(), param.getValue()[0]);
     }
     params.put("regions", regions);

     if (request.getParameter("show") != null) {

         List<String> likes = new ArrayList<String>();
         int i = 0;
         for (String keyword: KeywordExpressionParser.parseKeywordExpression(params.get("keyword").toString())) {
             keyword = keyword.trim();
             if (keyword.isEmpty()) {
                 continue;
             }
             params.put("keyword"+i, keyword);
             Collection<String> columns = new ArrayList<>();
             columns.add("mark");
             columns.add("model");
             if (useSubtitle) {
                 columns.add("subtitle");
             }
             if (useType) {
                 columns.add("type");
             }
             likes.add("upper(concat("+StringUtils.join(columns, ", ' ', ")+")) like upper(concat('%',:keyword" + i + ",'%'))");
             i ++;
         }
         String keywordMatching = "1=1";
         if (likes.size() > 0) {
             keywordMatching = StringUtils.join(likes, " or ");
         }
         params.remove("keyword");

         try {
             String yAxis = "mark, model";
             String yFormat = "<u>%s %s</u>";

             if (useSubtitle) {
                 yAxis += ", subtitle";
                 yFormat += "<br/> <span class='offer-field-subtitle'>%s</span>";
             }
             if (useType) {
                 yAxis += ", type";
                 yFormat += "<br/> <span class='offer-field-type'>%s</span>";
             }

             template.execute("set names utf8");
             List<Map<String, Object>> data = namedTemplate.query(
                     "" +
                             "select mark, model, type, subtitle, year, price, running, url " +
                             "from offers " +
                             "where parsed_at > date_add(now(), interval -1 * :maxOfferAgeMonth month) " +
                             "and city in (select name from city where id in (:regions)) " +
                             "and (" + keywordMatching + ") " +
                             "and year >= :minYear ", params, new MapListH2WrappingExtractor("x", "" +
                     "select "+yAxis+", year, " +
                     "cast(min(price) as int) prc_min, " +
                     "cast(max(price) as int) prc_max, " +
                     "cast(median(price) as int) prc_med, " +
                     "cast(min(running) as int) run_min, " +
                     "cast(max(running) as int) run_max, " +
                     "cast(median(running) as int) run_med, " +
                     "group_concat(url) urls, " +
                     "count(*) cnt " +
                     "from x " +
                     "group by "+yAxis+", year " +
                     "having cnt >= :minCount " +
                     "and prc_med >= :minPrice and prc_med <= :maxPrice " +
                     "order by year desc, "+yAxis, params));

             Pivot pivot = new Pivot();
             pivot.setData(data);
             pivot.setXAxis("year");
             pivot.setYAxis(yAxis);
             pivot.setYFormat(yFormat);
             pivot.setZAxis("prc_med, prc_min, prc_max, run_med, run_min, run_max, cnt, urls");
             pivot.setZFormat("<b>%,d</b> (%,d - %,d) руб <br/> <b>%,d</b> (%,d - %,d) км <br/> <b>%s</b> шт <span style='display: none;'>%s</span");
//             pivot.setZFormat("~%,.0fр, Δ%,.0fр, ~%,.0fкм, Δ%,.0fкм, %sшт");
             request.setAttribute("pivot", pivot);

             pageContext.include("generic_pivot.jsp");
         } catch (Exception e) {
             e.printStackTrace();
             e.printStackTrace(response.getWriter());
         }
     }

 %>
</body>
</html>
