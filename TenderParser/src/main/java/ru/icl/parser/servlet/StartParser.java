package ru.icl.parser.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        //на тот случай если сайт не доступен, вывод соответствующего сообщения на index.jsp 
        HttpSession session = request.getSession(true);
        session.setAttribute("status",null);
        ApplicationContext context = new ClassPathXmlApplicationContext("springContext.xml");
        BeanFactory factory = (BeanFactory) context;
        StartParser myBean = (StartParser) factory.getBean("startbean"); 
        try {
            StringBuilder httpResource = myBean.getHttpresource().getHttpResource(ResourceProcessorImpl.url);                        
            tenderList = myBean.getResourceprocessor().process(httpResource);                
            request.setAttribute("tend", tenderList);                                        
            request.getRequestDispatcher("index.jsp").forward(request, response);
            } catch (ServletException ex) {
                Logger.getLogger(StartParser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(StartParser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException ex) {
                Logger.getLogger(StartParser.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    session.setAttribute("status", "!!! Анализируемый сайт временно недоступен. Попробуйте позже");     
                    response.sendRedirect(request.getContextPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }    
}
