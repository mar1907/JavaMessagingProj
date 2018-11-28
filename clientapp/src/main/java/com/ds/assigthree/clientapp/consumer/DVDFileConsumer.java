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

public class DVDFileConsumer extends DefaultConsumer{

    private DvdDAO dvdDAO;
    private FileService fileService;

    public DVDFileConsumer(Channel channel, DvdDAO dvdDAO, FileService fileService) {
        super(channel);
        this.dvdDAO = dvdDAO;
        this.fileService = fileService;
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

            fileService.createFile(dvd.getTitle()+".txt", message);
            dvdDAO.addDVD(dvd);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bis.close();
        }

    }

}
