package com.ds.assigthree.adminapp.html;

import javax.servlet.http.HttpServletRequest;

public class HtmlPageBuilder {

    public static String getAdminHtml(HttpServletRequest req){
        String message = req.getSession().getAttribute("message") + "";

        String page = "<html>\n" +
                "<body>\n" +
                "\n" +
                "<form action=\"/admin\" method=\"POST\">\n" +
                "\tTitle: <input type=\"text\" name=\"name\"/>\n" +
                "\t<br>\n" +
                "\tYear: <input type=\"text\" name=\"year\"/>\n" +
                "\t<br>\n" +
                "\tPrice: <input type=\"text\" name=\"price\"/>\n" +
                "\t<br>\n" +
                "\t<input type=\"submit\" name=\"action\" value=\"Send\"/>\n" +
                "</form>\n" +
                "\n" +
                (req.getSession().getAttribute("message") == null ? "" : message) +
                "</body>\n" +
                "</html>";

        return page;
    }
}
