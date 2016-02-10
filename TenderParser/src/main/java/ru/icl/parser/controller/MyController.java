
package ru.icl.parser.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.icl.parser.model.Email;
import ru.icl.parser.model.Tender;
import ru.icl.parser.resource.HttpResource;
import ru.icl.parser.resource.ResourceCompare;
import ru.icl.parser.resource.ResourceProcessor;
import ru.icl.parser.resource.ResourceProcessorImpl;
import ru.icl.parser.resource.UrlAddress;
import ru.icl.parser.sendmail.MessageSender;
import ru.icl.parser.service.EmailService;
import ru.icl.parser.service.TenderService;

@Controller
public class MyController {
  
    private HttpResource httpresource;
    private ResourceProcessor resourceprocessor;
    private UrlAddress urladdress; 
    private ResourceCompare resourcecompare;
    private MessageSender messagesender; 

    public HttpResource getHttpresource() {
        return httpresource;
    }
    public void setHttpresource(HttpResource httpresource) {
        this.httpresource = httpresource;
    }
    public ResourceProcessor getResourceprocessor() {
        return resourceprocessor;
    }
    public void setResourceprocessor(ResourceProcessor resourceprocessor) {
        this.resourceprocessor = resourceprocessor;
    }
    public UrlAddress getUrladdress() {
        return urladdress;
    }
    public void setUrladdress(UrlAddress urladdress) {
        this.urladdress = urladdress;
    }
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
   
    public static String keyword = null;
    static List<Tender> tenderList = null;
    
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public String startParser(HttpServletRequest request, ModelMap map) {        
        
        tenderList = new ArrayList<Tender>();
        
        List<Tender> tdrList = null;
        tdrList = new ArrayList<Tender>();
        
        //на тот случай если сайт не доступен, вывод соответствующего сообщения на index.jsp 
        HttpSession session = request.getSession(true);
        session.setAttribute("status",null);
        
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BeanFactory factory = (BeanFactory) context;
        MyController myBean = (MyController) factory.getBean("startbean");                 
        
        TenderService serviceTender = (TenderService) context.getBean("tenderService");
        EmailService serviceEmail = (EmailService) context.getBean("emailService");
        List<Email> emailList = serviceEmail.getAll();       

        for (Email email : emailList) {
            keyword = email.getKeyword();
            String url = myBean.getUrladdress().getUrlAddress(ResourceProcessorImpl.urlSplit, 1, keyword);             
            try {
                StringBuilder httpResource = myBean.getHttpresource().getHttpResource(url);                
                tdrList = myBean.getResourceprocessor().process(httpResource);                  
                tenderList.addAll(tdrList);    
            } catch (NullPointerException ex) {
                Logger.getLogger(MyController.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute("status", "!!! Анализируемый сайт временно недоступен. Попробуйте позже");                                          
                return "redirect:/";                
            }
        } 
        map.addAttribute("tend", tenderList);
        return "index";
    }
    
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String compareAndSave(HttpServletRequest request, ModelMap map) {  
        
        HttpSession session = request.getSession(true);
        session.setAttribute("status","Список записанных в БД тендеров:");
        
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        TenderService serviceTender = (TenderService) context.getBean("tenderService");
        EmailService serviceEmail = (EmailService) context.getBean("emailService");  
        
        //данные, полученные из БД
        List<Tender> resourceDataBase = serviceTender.getAll();         
        
        //данные, полученные парсером при анализе сайта
        List<Tender> resourceProcessor = tenderList;  
                
        BeanFactory factory = (BeanFactory) context;
        MyController compareBean = (MyController) factory.getBean("startbean");        
        //resultList - результат сравнения resourceProcessor и resourceDataBase
//        resultList = new ArrayList<Tender>();
        List<Tender> resultList = compareBean.resourcecompare.getResourceWithoutDublicate(resourceProcessor, resourceDataBase); 
        
        Iterator<Tender> iteratorResultList = resultList.iterator();
        while(iteratorResultList.hasNext()) {
            Tender t = iteratorResultList.next();  
            System.out.println("DataBase ID " + serviceTender.getIdTender(t.getIdTender().toString()));
            if (serviceTender.getIdTender(t.getIdTender()).isEmpty()) {
                try {
                    serviceTender.save(t);
                }  catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("status","!!! Ошибка записи в БД");
                    return "redirect:/"; 
                }  
            }
            else {
                iteratorResultList.remove();
            } 
        } 
        
        if (!resultList.isEmpty()) {
            try {
                for (Email email : serviceEmail.getAll()) {
                    compareBean.messagesender.sendMessage("Список тендеров", "http://localhost:8080/ParserTender/view", email.getEmailEmploye());
                }
            } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("status","!!! Ошибка чтения email из БД / отправки email");
                    return "redirect:/"; 
            }
        }
        
        /*
        //сохранение resultList в БД
        if (!resultList.isEmpty()) {
            try {
                for(Tender t : resultList) {
                    serviceTender.save(t); 
                }    
            } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("status","!!! Ошибка записи в БД");
                    return "redirect:/"; 
            }
            
            try {
                for (Email email : serviceEmail.getAll()) {
//                    System.out.println("Email " + email.getEmailEmploye());
                    //отправка сообщения на почту
                    compareBean.messagesender.sendMessage("Список тендеров", "http://localhost:8080/ParserTender/view", email.getEmailEmploye());
                }
            } catch (Exception e) {
                    e.printStackTrace();
                    session.setAttribute("status","!!! Ошибка чтения email из БД / отправки email");
                    return "redirect:/"; 
            }
        }
        */
        
        
        
        map.addAttribute("tend", resultList);
        return "index";
    }
    
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewDataBase (HttpServletRequest request, ModelMap map) {  
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        TenderService serviceTender = (TenderService) context.getBean("tenderService");
        EmailService serviceEmail = (EmailService) context.getBean("emailService");
        List<Tender> tenderList = serviceTender.getAll();                
        map.addAttribute("tend", tenderList);
        return "view";
    }

}