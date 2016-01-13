package ru.icl.parser.service;

import ru.icl.parser.model.Tender;
import java.sql.SQLException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.icl.parser.dao.TenderDao;

@Service("tenderService")
public class TenderServiceImpl implements TenderService{
    
    @Autowired
    private TenderDao dao;

    @Transactional
    @Override
    public void save(Tender tender) {        
        dao.save(tender);
    }
    
    @Override
    public List<Tender> getAll() {
        return dao.getAll();
    }

    
}
