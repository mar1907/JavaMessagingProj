package com.ds.assigthree.clientapp.consumer;

import com.ds.assigthree.clientapp.dao.DvdDAO;
import com.ds.assigthree.clientapp.dao.EmailDAO;
import com.ds.assigthree.clientapp.service.FileService;
import com.ds.assigthree.clientapp.service.MailService;
import com.ds.assigthree.common.entity.DVD;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

public class DVDMailConsumer extends DefaultConsumer{

    private EmailDAO emailDAO;
    private MailService mailService;

    public DVDMailConsumer(Channel channel, EmailDAO emailDAO, MailService mailService) {
        super(channel);
        this.emailDAO = emailDAO;
        this.mailService = mailService;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(body);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);

            DVD dvd = (DVD)in.readObject();

            String message = "Title: " + dvd.getTitle()+
                    "\nYear: " + dvd.getYear()+
                    "\nPrice: " + dvd.getPrice();

            mailService.sendToList(emailDAO.findAllEmails(), "New dvd", message);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bis.close();
        }

    }
}
