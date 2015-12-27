package ru.icl.parser.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import ru.icl.parser.model.Email;
import ru.icl.parser.model.HibernateUtil;

public class EmailDaoImpl implements EmailDao {
    
    @Override
    public void addEmail(Email email) throws SQLException {
        Session sessionFactory = null;
        try{
            sessionFactory = HibernateUtil.getSessionFactory().openSession();
            sessionFactory.beginTransaction();
            sessionFactory.save(email);
            sessionFactory.getTransaction().commit();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if((sessionFactory != null) && (sessionFactory.isOpen()))
                sessionFactory.close();
        }    
    }    
    
    @Override
    public Email getEmail(int id) throws SQLException {
        Session sessionFactory = null;
        Email result = null;        
        try{
            sessionFactory = HibernateUtil.getSessionFactory().openSession();
            result = (Email) sessionFactory.get(Email.class, id);            
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if((sessionFactory != null) && (sessionFactory.isOpen()))
                sessionFactory.close();
        } 
        return result;   
    }

    @Override
    public List<Email> getEmails() throws SQLException {
        Session sessionFactory = null;        
        List<Email> emails = new ArrayList<Email>();               
        try{
            sessionFactory = HibernateUtil.getSessionFactory().openSession();
            emails = sessionFactory.createCriteria(Email.class).list();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if((sessionFactory != null) && (sessionFactory.isOpen()))
                sessionFactory.close();
        }
        return emails;
    }

    @Override
    public void deleteEmail(Email email) throws SQLException {
        Session sessionFactory = null;
        try{
            sessionFactory = HibernateUtil.getSessionFactory().openSession();
            sessionFactory.beginTransaction();
            sessionFactory.delete(email);
            sessionFactory.getTransaction().commit();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if((sessionFactory != null) && (sessionFactory.isOpen()))
                sessionFactory.close();
        }     
    }
    
}
