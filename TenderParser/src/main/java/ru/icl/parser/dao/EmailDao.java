package ru.icl.parser.dao;

import java.sql.SQLException;
import java.util.List;
import ru.icl.parser.model.Email;

public interface EmailDao {    
    public List<Email> getEmails() throws SQLException;    
}
