package ru.icl.parser.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.icl.parser.dao.EmailDao;
import ru.icl.parser.model.Email;

@Service("emailService")
@Transactional
public class EmailServiceImpl implements EmailService{

    @Autowired
    private EmailDao emaildao;

//    @Transactional
    @Override
    public void save(Email email) {
        emaildao.save(email);
    }

    @Override
    public List<Email> getAll() {
        return emaildao.getAll();        
    }
    
}
