package ru.icl.parser.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.icl.parser.dao.TenderDao;
import ru.icl.parser.model.Tender;
import ru.icl.parser.sendmail.MessageSender;
import ru.icl.parser.service.HttpResource;
import ru.icl.parser.service.ResourceProcessor;
import ru.icl.parser.service.ResourceProcessorImpl;

public class AllTender extends HttpServlet {

    private TenderDao tenderdao;
    private HttpResource httpresource;
    private ResourceProcessor resourceprocessor;
        
    public TenderDao getTenderdao() {
        return tenderdao;
    }
    public void setTenderdao(TenderDao tenderdao) {
        this.tenderdao = tenderdao;
    }    
    
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {  
        
        ApplicationContext context = new ClassPathXmlApplicationContext("springContext.xml");
        BeanFactory factory = (BeanFactory) context;
        AllTender myBean = (AllTender) factory.getBean("mainbean");
        StringBuilder httpResource = myBean.getHttpresource().getHttpResource(ResourceProcessorImpl.url);
        List<Tender> tenderList = myBean.getResourceprocessor().process(httpResource);
        request.setAttribute("tend", tenderList);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }    

}
