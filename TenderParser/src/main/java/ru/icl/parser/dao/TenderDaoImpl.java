package ru.icl.parser.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.icl.parser.model.Tender;

//@Repository
public class TenderDaoImpl implements TenderDao{
    
    @Autowired
    SessionFactory sFactory;
    
    @Override
    public void addTender(Tender tender) throws SQLException {
        sFactory.getCurrentSession().save(tender);            
    }

    @Override
    public Tender getTender(int id) throws SQLException {        
        Tender result = null;        
        result = (Tender) sFactory.getCurrentSession().get(Tender.class, id);            
        return result;  
    }

    @Override
    public List<Tender> getTenders() throws SQLException {        
        List<Tender> tenders = new ArrayList<Tender>();               
        tenders = sFactory.getCurrentSession().createCriteria(Tender.class).list();       
        return tenders;
    }
    

//    @Override
//    public void addTender(Tender tender) throws SQLException {
//        Session sessionFactory = null;
//        try{
//            sessionFactory = HibernateUtil.getSessionFactory().openSession();
//            sessionFactory.beginTransaction();            
//            sessionFactory.save(tender);
//            sessionFactory.getTransaction().commit();
//        } catch(Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if((sessionFactory != null) && (sessionFactory.isOpen()))
//                sessionFactory.close();                
//        }    
//    }
//
//    @Override
//    public Tender getTender(int id) throws SQLException {
//        Session sessionFactory = null;
//        Tender result = null;        
//        try{           
//            sessionFactory = HibernateUtil.getSessionFactory().openSession();
//            result = (Tender) sessionFactory.get(Tender.class, id);            
//        } catch(Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if((sessionFactory != null) && (sessionFactory.isOpen()))
//                sessionFactory.close();
//        } 
//        return result;  
//    }
//
//    @Override
//    public List<Tender> getTenders() throws SQLException {
//        Session sessionFactory = null;        
//        List<Tender> tenders = new ArrayList<Tender>();               
//        try{
//            sessionFactory = HibernateUtil.getSessionFactory().openSession();
//            tenders = sessionFactory.createCriteria(Tender.class).list();
//        } catch(Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if((sessionFactory != null) && (sessionFactory.isOpen()))
//                sessionFactory.close();
//        }
//        return tenders;
//    }
    
}
