package ru.icl.parser.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.icl.parser.model.Tender;
import ru.icl.parser.resource.ResourceCompare;
import ru.icl.parser.sendmail.MessageSender;
import ru.icl.parser.service.EmailService;
import ru.icl.parser.service.TenderService;
import static ru.icl.parser.servlet.StartParser.tenderList;

public class CompareAndSave extends HttpServlet {
    
    private ResourceCompare resourcecompare;
    private MessageSender messagesender;        
    public ResourceCompare getResourcecompare() {
        return resourcecompare;
    }
    public void setResourcecompare(ResourceCompare resourcecompare) {
        this.resourcecompare = resourcecompare;
    }
    public MessageSender getMessagesender() {
        return messagesender;
    }
    public void setMessagesender(MessageSender messagesender) {
        this.messagesender = messagesender;
    }
        
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        session.setAttribute("status","Список записанных в БД тендеров:");
        
        ApplicationContext context = new ClassPathXmlApplicationContext("springContext.xml");
        TenderService serviceTender = (TenderService) context.getBean("tenderService");
        EmailService serviceEmail = (EmailService) context.getBean("emailService");  
        //данные, полученные из БД
        List<Tender> resourceDataBase = serviceTender.getAll();
        
        //данные, полученные парсером при анализе сайта
        List<Tender> resourceProcessor = StartParser.tenderList;
                
        BeanFactory factory = (BeanFactory) context;
        CompareAndSave myBean = (CompareAndSave) factory.getBean("savebean");        
        //resultList - результат сравнения resourceProcessor и resourceDataBase
        List<Tender> resultList = myBean.resourcecompare.getResourceWithoutDublicate(resourceProcessor, resourceDataBase);
              
        if (!resultList.isEmpty()) {
            System.out.println("ResultList не пустой");
            //сохранение resultList в БД
            for(Tender tendr : resultList) {
                serviceTender.save(tendr);
            } 
        }

        //отправка сообщения на почту
        myBean.messagesender.sendMessage("Список тендеров", "http://localhost:8080/ParserTender/view", "rchumarin@gmail.com");
                
        request.setAttribute("tend", resultList);        
        try {            
            request.getRequestDispatcher("index.jsp").forward(request, response);            
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(StartParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(StartParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }           
}
