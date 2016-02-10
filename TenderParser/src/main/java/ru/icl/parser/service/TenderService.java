package ru.icl.parser.service;

import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.icl.parser.model.Tender;

@Service
public interface TenderService {
    public void save(Tender tender);        
    public List<Tender> getAll();
    public String getIdTender(String IdTender);
}
