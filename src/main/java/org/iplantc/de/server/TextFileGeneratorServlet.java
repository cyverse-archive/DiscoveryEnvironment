package org.iplantc.de.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * A Servlet that takes a post request and generates a text file out the post body
 * 
 * @author sriram
 * 
 */
public class TextFileGeneratorServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 3948905615394166741L;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        InputStream is = null;
        ServletOutputStream op = null;

        try {
            is = request.getInputStream();
            op = response.getOutputStream();
            if (is != null) {
                DataInputStream ds = new DataInputStream(is);

                byte[] charBuffer = new byte[128];
                int bytesRead = -1;
                response.setContentType("text/plain");
                response.setContentLength(request.getContentLength());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + "app-josn.txt"
                        + "\"");

                while ((bytesRead = ds.read(charBuffer)) > 0) {
                    op.write(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException("Unable to process your request! Please try again later.");
        }
    }
}
