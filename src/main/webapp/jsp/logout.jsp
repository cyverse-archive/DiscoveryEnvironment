You have been logged out of <b><%= request.getParameter("appName") %> </b>
To logout of all applications, click
<%
 
session.invalidate();
String logoutPath = application.getInitParameter("org.iplantc.logout-url");
String logoutForward = application.getInitParameter("org.iplantc.logout-forward");
out.print("<a href='" + logoutPath + "?service=" + logoutForward + "'>here</a>");
 
%>