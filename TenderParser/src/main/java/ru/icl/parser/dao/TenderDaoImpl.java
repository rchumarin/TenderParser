package ru.icl.parser.dao;

import ru.icl.parser.model.Tender;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TenderDaoImpl implements TenderDao{ 
    
    @PersistenceContext
    private EntityManager em;
    
    public void save(Tender tender) {
        try {
            em.persist(tender);                   
        } catch (Exception e) {
            System.out.println("Oшибка метода  TenderDaoImpl.save");
            e.printStackTrace();
        }    
    }    
    
    public List<Tender> getAll(){        
        return em.createQuery("from Tender", Tender.class).getResultList();
    }
}  