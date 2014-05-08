<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%><%@ page import="net.kkolyan.pivot.net.kkolyan.cars2.autoru.OffersImportTask"%><%@ page import="org.springframework.beans.BeansException"%><%@ page import="java.io.IOException"%><%@ page contentType="text/plain;charset=UTF-8" language="java" %>
<%
try {
    long interval = 1000;
    if (request.getParameter("interval") != null) {
        interval = Long.parseLong(request.getParameter("interval"));
    }
    OffersImportTask task = WebApplicationContextUtils.getRequiredWebApplicationContext(application).getBean(OffersImportTask.class);
    task.doImport(response.getWriter(), interval);
    %>
    OK
    <%
} catch (Exception e) {
    e.printStackTrace(response.getWriter());
}
%>
