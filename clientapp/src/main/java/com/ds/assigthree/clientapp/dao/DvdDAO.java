package com.ds.assigthree.clientapp.dao;

import com.ds.assigthree.common.entity.DVD;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DvdDAO {

    private SessionFactory sessionFactory;

    public DvdDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public DVD addDVD(DVD dvd){
        int id = -1;
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            id = (Integer) session.save(dvd);
            dvd.setId(id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null){
                tx.rollback();
            }
        } finally {
            session.close();
        }

        return dvd;
    }

}
