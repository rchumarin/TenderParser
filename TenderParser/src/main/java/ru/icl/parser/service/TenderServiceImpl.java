package ru.icl.parser.service;

import ru.icl.parser.model.Tender;
import java.sql.SQLException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.icl.parser.dao.TenderDao;

@Service("tenderService")
//@Service
public class TenderServiceImpl implements TenderService{
        
    @Autowired
    private TenderDao dao;

    @Transactional    
    public void save(Tender tender) {        
        try {
            dao.save(tender);
        
        } catch (Exception e) {
            System.out.println("Oшибка метода  TenderServiceImpl");
            e.printStackTrace();
        }         
    }
    
    @Transactional
    public List<Tender> getAll() {
        return dao.getAll();
    }
    
    @Transactional
    public String getIdTender(String IdTender) {
        return dao.getIdTender(IdTender);
    }
  
}
