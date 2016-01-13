package ru.icl.parser.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.icl.parser.model.Tender;
import ru.icl.parser.resource.HttpResource;
import ru.icl.parser.resource.ResourceProcessor;
import ru.icl.parser.resource.ResourceProcessorImpl;
import ru.icl.parser.service.EmailService;
import ru.icl.parser.service.TenderService;

public class StartParser extends HttpServlet { 
    
    private HttpResource httpresource;
    private ResourceProcessor resourceprocessor;            
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
    
    static List<Tender> tenderList = null;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
       
        ApplicationContext context = new ClassPathXmlApplicationContext("springContext.xml");
        BeanFactory factory = (BeanFactory) context;
        StartParser myBean = (StartParser) factory.getBean("startbean"); 
        StringBuilder httpResource = myBean.getHttpresource().getHttpResource(ResourceProcessorImpl.url);                        
        tenderList = myBean.getResourceprocessor().process(httpResource);                
        request.setAttribute("tend", tenderList);        
        try {            
            request.getRequestDispatcher("index.jsp").forward(request, response);            
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(StartParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(StartParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
