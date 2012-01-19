
#Belphegor

Belphegor is the app administration console for the Discovery Environment.

## Deploying Belphegor

Belphegor is currently packaged as a WAR file, so it can be deployed in any
existing servlet container.

## Configuring Belphegor

The Belphegor configuration is stored in two separate files.  Most of the
configuration parameters are stored in ${BELPHEGOR_HOME}/WEB-INF/web.xml

### ${BELPHEGOR_HOME}/WEB-INF/web.xml

1. The context parameter, org.iplantc.logout-url, must be changed to point
   to the correct CAS authentication server.  The web.xml template that is
   checked into the source code management (SCM) repository has this parameter
   set to https://cas-server.iplantcollaborative.org/cas/logout.  This should
   be changed to point to the CAS server that is being used.
2. If desired, the context parameter, org.iplantc.logout-forward, may be
   changed to point to a different URL.  This is the URL to which the user
   will be forwarded if the user chooses to log out of all applications after
   logging out of Belphegor.  The web.xml template in the SCM repository has
   this parameter set to http://iplantcollaborative.org by default.
3. The initialization parameter, casServerLoginUrl, for the filter, CAS
   Authentication Filter, must be changed to point to the correct CAS
   authentication server.  The web.xml template that is checked into the SCM
   repository has this parameter set to
   https://cas-server.iplantcollaborative.org/cas/login.  This should be
   changed to point to the CAS server that is being used.
4. The initialization parameter, serverName, for the filter, CAS Authentication
   Filter, must be changed to point to the local host.  The webg.xml template
   that is checked into the SCM repository has this parameter set to
   https://cas-client.iplantcollaborative.org.  The host name in this parameter
   should be changed to the fully qualified domain name of the local host.  If
   the host is not listed in the domain name service then an IP address may
   also be used.  Note that the protocol may need to be changed to HTTP if
   HTTPS is not being used.  Also note that the port must be specified if a
   non-standard port is being used.  For example, if HTTP is being used over
   port 8080 and the host name is foo.bar.org then this parameter should be
   set to http://foo.bar.org:8080.
5. The initialization parameter, casServerUrlPrefix, for the filter, CAS
   Validation Filter, must be changed to point to the correct CAS
   authentication server.  The web.xml template that is checked into the SCM
   repository has this parameter set to
   https://cas-server.iplantcollaborative.org/cas.  This should be changed to
   point to the CAS server that is being used.
6. The initialization parameter, serverName, for the filter, CAS Validation
   Filter, must be changed to point to the local host.  The value of this
   parameter should be the same as the value of the serverName parameter for
   CAS Authentication Filter.
7. The initialization parameter, proxyCallbackUrl, for the filter, CAS
   Validation Filter must be changed to point to the local host and context
   path.  The web.xml template that is checked into the SCM repository has
   this parameter set to
   https://cas-client.iplantcollaborative.org/belphegor/proxy-receptinator.
   This should be changed to point to the local host.  The leading portion
   of this URL should be the same as in the serverName parameters for the
   CAS Authentication Filter and the CAS Validation Filter.  Note that the
   context path in this URL (belphegor, by default) should be changed to
   match the context path that is actually being used.  For example, if the
   serviceName parameter is set to http://foo.bar.org:8080 and the context
   path is belphegor-fu then the value of this parameter should be set to
   http://foo.bar.org:8080/belphegor-fu/proxy-receptinator.  The portion of
   the URL after the context path may be changed, but this is not necessary.
   If the portion of the URL after the context path is changed then the value
   of the initialization parameter, proxyReceptorUrl, must be changed to
   match.
8. If desired, the name of the role (which is the name of an LDAP group for
   our CAS server configuration) that is authorized to access Belphegor may
   be changed.  This role name is specified by the initialization parameter,
   authorizedRole, for the filter, roleFilter.  The web.xml template that is
   checked into the SCM repository has this parameter set to dev, meaning that
   only users in the dev group will be permitted to access Belphegor.
9. If the context path is anything other than /belphegor, then the fourth URL
   pattern in the filter mapping for the filter, CAS Authentication Filter,
   must be edited to match the correct context path.  The web.xml template
   that is checked into the SCM repository has this path set to
   /belphegor/belphegor/*.  If, for example, the context path is /belphegor-fu
   then this path should be set to /belphegor-fu/belphegor/*.
10. If the context path is anything other than /belphegor, then the fourth URL
    pattern in the filter mapping for the filter, roleFilter, will have to be
    edited to match the correct context path.  The web.xml template that is
    checked into the SCM repository has this path set to
    /belphegor/belphegor/*.  If, for example, the context path is
    /belphegor-fu then this path should be set to /belphegor-fu/belphegor/*.

### ${BELPHEGOR_HOME}/WEB-INF/classes/belphegor.properties

1. The base URL used to connect to the back-end services for Belphegor (which
   are named, Conrad) must be modified to point to the right machine.  The
   belphegor.properties template that is checked into the SCM repository has
   this parameter set to http://localhost/conrad.  In general, the host name
   in this URL should be changed to the fully qualified domain name of the
   host where the services are deployed and a port number should be added to
   the URL if a non-standard port is being used.  Note that the protocol may
   have to be changed if, for example, HTTPS is being used.  Also note that
   the use of HTTPS is recommended in production environments.

## Activating the Configuration Changes

Once the configuration changes have been made it is necessary to bounce the
servlet container (for example, Apache Tomcat or Jetty) for the changes to
take effect.  If the Belphegor web application fails to start, it's helpful
to review the configuration files that have been modified and to examine the
servlet container's log files.  For example, Apache Tomcat, will log
deployment errors in ${TOMCAT_HOME}/logs/catalina.out and
${TOMCAT_HOME}/logs/localhost*.log.
