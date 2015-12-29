package ru.icl.parser.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.icl.parser.model.Email;

//@Repository
public class EmailDaoImpl implements EmailDao {
    
    @Autowired
    SessionFactory sFactory;
    
    @Override
    public List<Email> getEmails() throws SQLException {              
        List<Email> emails = new ArrayList<Email>();                       
        emails = sFactory.getCurrentSession().createCriteria(Email.class).list();        
        return emails;
    }
   
//    @Override
//    public List<Email> getEmails() throws SQLException {
//        Session sessionFactory = null;        
//        List<Email> emails = new ArrayList<Email>();               
//        try{
//            sessionFactory = HibernateUtil.getSessionFactory().openSession();
//            emails = sessionFactory.createCriteria(Email.class).list();
//        } catch(Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if((sessionFactory != null) && (sessionFactory.isOpen()))
//                sessionFactory.close();
//        }
//        return emails;
//    }
    
}
