package ru.icl.parser.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.icl.parser.model.Email;

@Repository
public class EmailDaoImpl implements EmailDao {
    
    @PersistenceContext
    private EntityManager em;
    
    public List<Email> getAll(){                                            
        return em.createQuery("from Email", Email.class).getResultList();        
    }

    public void save(Email email) {
        em.persist(email);
    }    
}
