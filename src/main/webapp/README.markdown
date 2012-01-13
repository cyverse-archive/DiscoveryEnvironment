
#Belphegor
Belphegor is the app administration console for the Discovery Environment.

## Deploying Belphegor
Belphegor is currently packaged as a WAR file, so it can be deployed in any
existing servlet container.

## Configuring Belphegor
Once Belphegor is deployed into a servlet container and the war is exploded, perform the following steps.

1. cd into  <app-name>/WEB-INF/
2. use your favorite text/xml editor to start editing web.xml
3. set the <param-value> for <context-param> org.iplantc.logout-url to appropriate logout url provided by the CAS server. For example the logout url for our CAS server in auto.iplantcollaborative.org is https://auth.iplantcollaborative.org/cas/logout
4. (optional) set the <param-value> for <context-param> org.iplantc.logout-forward to appropriate url. By default, it is set to http://iplantcollaborative.org
5. set CAS Authentication Filter param casServerLoginUrl to appropriate CAS login  url. For example the logout url for our CAS server in auto.iplantcollaborative.org is https://auth.iplantcollaborative.org/cas/login
6. set CAS Authentication Filter param serverName to DNS (In cases where the server is not listed in the DNS then an IP address will work fine along with port number if applicable) of the machine where this application is being deployed
7. set CAS Validation Filter param casServerUrlPrefix to appropriate CAS server prefix. For example, https://auth.iplantcollaborative.org/cas
8. set CAS Validation Filter param serverName to DNS (In cases where the server is not listed in the DNS then an IP address will work fine along with port number if applicable) of the machine where this application is being deployed
9. update the protocol, host, port and context path in the CAS Validation Filter parameter, proxyCallbackUrl.  It is not necessary (and not recommended) to change the remainder of the path (/proxy-receptinator).  If you do change this portion of the path then it is also necessary to change the configuration parameter, proxyReceptorUrl.
10. save web.xml.
11. cd into <app-name>/WEB-INF/classes  and start editing belphegor.properties
12. configure Belphegor to talk to appropriate Conrad services
13. Save belphegor.properties
14. Bounce the application server.
