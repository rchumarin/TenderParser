package ru.icl.parser.service;

import java.util.List;
import ru.icl.parser.model.Email;

public interface EmailService {
    public void save(Email email);
    public List<Email> getAll();    
}
