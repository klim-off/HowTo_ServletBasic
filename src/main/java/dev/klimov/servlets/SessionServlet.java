package dev.klimov.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;

public class SessionServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(SessionServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("do get " + request);
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html");  // HTML 5
            out.println("<html><head>");
            out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
            String title = "Title";
            out.println("<head><title>" + title + "</title></head>");
            out.println("<body>");
            out.println("<h3>" + title + "</h3>");

            // Return the existing session if there is one. Otherwise, create a new session
            HttpSession session = request.getSession();

            // Display session information
            out.println(" session id " + session.getId() + "<br />");
            out.println("created :  ");
            out.println(new Date(session.getCreationTime()) + "<br />");
            out.println("last accessed:  ");
            out.println(new Date(session.getLastAccessedTime()) + "<br /><br />");

            // Set an attribute (name-value pair) if present in the request
            String attName = request.getParameter("attribute_name");
            if (attName != null) attName = attName.trim();
            String attValue = request.getParameter("attribute_value");
            if (attValue != null) attValue = attValue.trim();
            if (attName != null && !attName.equals("")
                    && attValue != null && !attValue.equals("")) {
                // synchronized session object to prevent concurrent update
                synchronized (session) {
                    session.setAttribute(attName, attValue);
                }
            }

            // Display the attributes (name-value pairs) stored in this session
            out.println("data <br>");
            Enumeration names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String value = session.getAttribute(name).toString();
                out.println(filter(name) + " = "
                        + filter(value) + "<br>");
            }
            out.println("<br />");

            // Display a form to prompt user to create session attribute
            out.println("<form method='get'>");
            out.println("key: ");
            out.println("<input type='text' name='attribute_name'><br />");
            out.println("value: ");
            out.println("<input type='text' name='attribute_value'><br />");
            out.println("<input type='submit' value='SEND'>");
            out.println("</form><br />");

            out.print("<a href='");
            // Encode URL by including the session ID (URL-rewriting)
            out.print(response.encodeURL(request.getRequestURI() + "?attribute_name=foo&attribute_value=bar"));
            out.println("'>Encode URL with session ID (URL re-writing)</a>");
            out.println("</body></html>");
        }
        // Always close the output writer
    }

    public static String filter(String message) {
        if (message == null) return null;
        int len = message.length();
        StringBuilder result = new StringBuilder(len + 20);
        char aChar;

        for (int i = 0; i < len; ++i) {
            aChar = message.charAt(i);
            switch (aChar) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                default:
                    result.append(aChar);
            }
        }
        return (result.toString());
    }
}
