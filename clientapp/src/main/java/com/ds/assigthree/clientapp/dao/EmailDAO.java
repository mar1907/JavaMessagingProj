package com.ds.assigthree.clientapp.dao;

import com.ds.assigthree.clientapp.entity.Email;
import org.hibernate.*;

import java.util.ArrayList;
import java.util.List;

public class EmailDAO {

    private SessionFactory sessionFactory;

    public EmailDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Email> findAllEmails(){
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Email> emails = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Email");
            emails = query.list();
            tx.commit();
        } catch (HibernateException e){
            if(tx!=null){
                tx.rollback();
            }
        } finally {
            session.close();
        }

        return emails;
    }
}
