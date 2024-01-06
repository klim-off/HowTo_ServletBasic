package dev.klimov.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class RequestInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html");  // HTML 5
        out.println("<html><head>");
        out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
        String title = "My title Maksim";
        out.println("<head><title>" + title + "</title></head>");
        out.println("<body>");
        out.println("<h3>" + title + "</h3>");

        // Tabulate the request information
        out.println("<table>");
        out.println("<tr><td> Protocol </td>");
        out.println("<td>" + request.getProtocol() + "</td></tr>");
        out.println("<tr><td>Method</td>");
        out.println("<td>" + request.getMethod() + "</td></tr>");
        out.println("</td></tr><tr><td>");
        out.println("<tr><td>URI</td>");
        out.println("<td>" + filter(request.getRequestURI()) + "</td></tr>");
        out.println("<tr><td>Path info</td>");
        out.println("<td>" + filter(request.getPathInfo()) + "</td></tr>");
        out.println("<tr><td>Path Translated:</td>");
        out.println("<td>" + request.getPathTranslated() + "</td></tr>");
        out.println("<tr><td>remote addres</td>");
        out.println("<td>" + request.getRemoteAddr() + "</td></tr>");

        StringBuilder lineToReturn = new StringBuilder();
        request.getParameterMap().forEach((key,value)->
                                lineToReturn.append(key)
                                 .append(" = ")
                                 .append(Arrays.toString(value)));
        out.println("<tr><td>params map</td>");
        out.println("<td>" + lineToReturn + "</td></tr>");

        out.println("<tr><td>query string</td>");
        out.println("<td>" + request.getQueryString() + "</td></tr>");

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
