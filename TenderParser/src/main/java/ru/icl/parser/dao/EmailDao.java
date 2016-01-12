package ru.icl.parser.dao;

import java.sql.SQLException;
import java.util.List;
import ru.icl.parser.model.Email;

public interface EmailDao { 
    public void save(Email email);
    public List<Email> getAll(); 
}
