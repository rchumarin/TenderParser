package ru.icl.parser.dao;

import java.sql.SQLException;
import java.util.List;
import ru.icl.parser.model.Tender;

public interface TenderDao {
    public void addTender(Tender tender) throws SQLException;    
    public Tender getTender(int id) throws SQLException;
    public List<Tender> getTenders() throws SQLException;
}
