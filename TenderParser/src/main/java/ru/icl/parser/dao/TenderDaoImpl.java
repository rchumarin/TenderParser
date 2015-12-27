package ru.icl.parser.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import ru.icl.parser.model.HibernateUtil;
import ru.icl.parser.model.Tender;

public class TenderDaoImpl implements TenderDao{

    @Override
    public void addTender(Tender tender) throws SQLException {
        Session sessionFactory = null;
        try{
            sessionFactory = HibernateUtil.getSessionFactory().openSession();
            sessionFactory.beginTransaction();            
            sessionFactory.save(tender);
            sessionFactory.getTransaction().commit();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if((sessionFactory != null) && (sessionFactory.isOpen()))
                sessionFactory.close();                
        }    
    }

    @Override
    public Tender getTender(int id) throws SQLException {
        Session sessionFactory = null;
        Tender result = null;        
        try{           
            sessionFactory = HibernateUtil.getSessionFactory().openSession();
            result = (Tender) sessionFactory.get(Tender.class, id);            
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if((sessionFactory != null) && (sessionFactory.isOpen()))
                sessionFactory.close();
        } 
        return result;  
    }

    @Override
    public List<Tender> getTenders() throws SQLException {
        Session sessionFactory = null;        
        List<Tender> tenders = new ArrayList<Tender>();               
        try{
            sessionFactory = HibernateUtil.getSessionFactory().openSession();
            tenders = sessionFactory.createCriteria(Tender.class).list();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if((sessionFactory != null) && (sessionFactory.isOpen()))
                sessionFactory.close();
        }
        return tenders;
    }
    
}
