package com.ds.assigthree.adminapp.servlet;

import com.ds.assigthree.adminapp.html.HtmlPageBuilder;
import com.ds.assigthree.common.entity.DVD;
import com.ds.assigthree.common.entity.DVDBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class AdminServlet extends HttpServlet {

    private static final String EXCHANGE_NAME = "dvd";

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    @Override
    public void init() throws ServletException {

        try {
            factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        } catch (Exception e){
            e.printStackTrace();
            return;
        }
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println(HtmlPageBuilder.getAdminHtml(req));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
            DVD dvd = new DVDBuilder()
                    .setTitle(req.getParameter("name"))
                    .setYear(Integer.parseInt(req.getParameter("year")))
                    .setPrice(Double.parseDouble(req.getParameter("price")))
                    .build();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;

            try{
                out = new ObjectOutputStream(bos);
                out.writeObject(dvd);
                out.flush();

                byte[] dvdBytes = bos.toByteArray();
                channel.basicPublish(EXCHANGE_NAME, "", null, dvdBytes);

                req.getSession().setAttribute("message", "Success!");
            } catch (Exception e){
                req.getSession().setAttribute("message", "A problem occured when trying to send to the queue");
            } finally {
                bos.close();
            }

        } catch (NumberFormatException e){
            req.getSession().setAttribute("message", "Parse error!");
        }

        resp.sendRedirect("/admin");
    }
}
