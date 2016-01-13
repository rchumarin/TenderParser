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
    
    @Override
    public void save(Tender tender) {
        em.persist(tender);                   
    }    

    @Override
    public List<Tender> getAll(){        
        return em.createQuery("from Tender", Tender.class).getResultList();
    }
}  