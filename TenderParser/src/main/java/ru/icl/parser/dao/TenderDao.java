package ru.icl.parser.dao;

import ru.icl.parser.model.Tender;
import java.util.List;

public interface TenderDao {
    public void save(Tender tender);        
    public List<Tender> getAll();
    public String getIdTender(String IdTender);
}
