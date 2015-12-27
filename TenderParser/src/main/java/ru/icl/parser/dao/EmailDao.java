package ru.icl.parser.dao;

import java.sql.SQLException;
import java.util.List;
import ru.icl.parser.model.Email;

public interface EmailDao {
    public void addEmail(Email email) throws SQLException;
    public Email getEmail(int id) throws SQLException;
    public List<Email> getEmails() throws SQLException;
    public void deleteEmail(Email email) throws SQLException;
}
