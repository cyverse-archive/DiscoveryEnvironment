package org.iplantc.de.server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class DispatchServlet extends HttpServlet {
    /**
     * The universal identifier for this version of this class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Used to log debugging messages.
     */
    private static final Logger LOG = Logger.getLogger(DispatchServlet.class);

    /**
     * A set of header fields not to copy.
     */
    private static final HashSet<String> HEADERS_NOT_TO_COPY = new HashSet<String>();
    {
        HEADERS_NOT_TO_COPY.add("proxy-connection"); //$NON-NLS-1$
        HEADERS_NOT_TO_COPY.add("connection"); //$NON-NLS-1$
        HEADERS_NOT_TO_COPY.add("keep-alive"); //$NON-NLS-1$
        HEADERS_NOT_TO_COPY.add("transfer-encoding"); //$NON-NLS-1$
        HEADERS_NOT_TO_COPY.add("te"); //$NON-NLS-1$
        HEADERS_NOT_TO_COPY.add("trailer"); //$NON-NLS-1$
        HEADERS_NOT_TO_COPY.add("proxy-authorization"); //$NON-NLS-1$
        HEADERS_NOT_TO_COPY.add("proxy-authenticate"); //$NON-NLS-1$
        HEADERS_NOT_TO_COPY.add("upgrade"); //$NON-NLS-1$
    }

    /**
     * Used to establish connections to remote services.
     */
    private UrlConnector urlConnector;

    /**
     * Initializes a new servlet.
     */
    public DispatchServlet() {
        urlConnector = new AuthenticatedUrlConnector();
    }

    /**
     * {@inheritDoc}
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        LOG.debug("received dispatch request"); //$NON-NLS-1$
        String address = extractAddress(request);
        logAddress(address);
        HttpURLConnection urlConnection = openConnection(request, address);
        LOG.debug("connection to URL established"); //$NON-NLS-1$
        copyHeaders(urlConnection, response);
        LOG.debug("HTTP headers copied"); //$NON-NLS-1$
        retrieveResult(urlConnection, response);
        LOG.debug("result retrieved"); //$NON-NLS-1$
    }

    /**
     * Copies the HTTP headers from the given URL connection to this servlet's response.
     * 
     * @param urlConnection the connection to the remote service.
     * @param response this servlet's response.
     */
    private void copyHeaders(HttpURLConnection urlConnection, HttpServletResponse response) {
        Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
        for (String name : headerFields.keySet()) {
            if (name != null && !HEADERS_NOT_TO_COPY.contains(name.toLowerCase())) {
                for (String value : headerFields.get(name)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("adding response header: " + name + " = " + value); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    response.addHeader(name, value);
                }
            }
        }
    }

    /**
     * Logs the dispatch address if debugging is enabled.
     * 
     * @param address the dispatch address.
     */
    private void logAddress(String address) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("dispatch address: " + address); //$NON-NLS-1$
        }
    }

    /**
     * Retrieves the result from the given URL connection.
     * 
     * @param urlConnection the connection to the remote service.
     * @param response this servlet's response.
     * @throws ServletException if an I/O error occurs.
     */
    private void retrieveResult(HttpURLConnection urlConnection, HttpServletResponse response)
            throws ServletException {
        try {
            IOUtils.copy(urlConnection.getInputStream(), response.getOutputStream());
        } catch (IOException e) {
            throw new ServletException("unable to retrieve the result", e); //$NON-NLS-1$
        }
    }

    /**
     * Extracts the dispatch address from the servlet request.
     * 
     * @param request the servlet request.
     * @return the address.
     * @throws ServletExcepion if the address wasn't provided.
     */
    private String extractAddress(HttpServletRequest request) throws ServletException {
        String address = request.getParameter("address"); //$NON-NLS-1$
        if (address == null) {
            throw new ServletException("required parameter, address, not provided"); //$NON-NLS-1$
        }
        return address;
    }

    /**
     * Opens the connection to the remote address.
     * 
     * @param request the servlet request.
     * @param address the address to connect to.
     * @return the connection.
     * @throws ServletException if the connection can't be established.
     */
    private HttpURLConnection openConnection(HttpServletRequest request, String address)
            throws ServletException {
        try {
            return urlConnector.getUrlConnection(request, address);
        } catch (IOException e) {
            throw new ServletException("unable to open the connection", e); //$NON-NLS-1$
        }
    }
}
