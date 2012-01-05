You have been logged out of <b><%= request.getParameter("appName") %> </b>
To logout of all applications, click
<%
 
session.invalidate();
out.print("<a href='https://auth.iplantcollaborative.org/cas/logout?service=http://iplantcollaborative.org'>here</a>");
 
%>