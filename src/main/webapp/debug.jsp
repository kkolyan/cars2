<%@ page import="org.springframework.jdbc.core.JdbcTemplate" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<%

    JdbcTemplate template = WebApplicationContextUtils.getRequiredWebApplicationContext(application).getBean(JdbcTemplate.class);
%>
<%=((MysqlConnectionPoolDataSource)template.getDataSource()).getUrl()%>

</body>
</html>
