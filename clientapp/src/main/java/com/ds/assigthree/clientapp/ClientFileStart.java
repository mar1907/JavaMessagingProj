package com.ds.assigthree.clientapp;

import com.ds.assigthree.clientapp.consumer.DVDFileConsumer;
import com.ds.assigthree.clientapp.consumer.DVDMailConsumer;
import com.ds.assigthree.clientapp.dao.DvdDAO;
import com.ds.assigthree.clientapp.dao.EmailDAO;
import com.ds.assigthree.clientapp.service.FileService;
import com.ds.assigthree.clientapp.service.MailService;
import com.rabbitmq.client.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ClientFileStart {

    private static final String EXCHANGE_NAME = "dvd";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        DvdDAO dvdDAO = new DvdDAO(sessionFactory);

        FileService fileService = new FileService();

        Consumer fileConsumer = new DVDFileConsumer(channel, dvdDAO, fileService);
        channel.basicConsume(queueName, true, fileConsumer);


    }
}
