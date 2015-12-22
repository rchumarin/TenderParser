package ru.icl.parser.main;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.icl.parser.resource.HttpResource;
import ru.icl.parser.resource.ResourceProcessor;
import ru.icl.parser.resource.ResourceProcessorImpl;
import ru.icl.parser.sendmail.*;

public class Main {
    
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
    
    private MessageSender sendertls;
    public MessageSender getSendertls() {
        return sendertls;
    }
    public void setSendertls(MessageSender sendertls) {
        this.sendertls = sendertls;
    }
    
    
    public static void main(String[] args) throws IOException { 
        
        ApplicationContext context = new ClassPathXmlApplicationContext("springContext.xml");
        BeanFactory factory = (BeanFactory) context;
        Main myBean = (Main) factory.getBean("mainbean");
        StringBuilder httpResource = myBean.getHttpresource().getHttpResource(ResourceProcessorImpl.url);
        List tenders = myBean.getResourceprocessor().process(httpResource);
        
//        for (Object tender : tenders) System.out.println(tender.toString());                 
        
//        myBean.getSendertls().sendMessage("This is Subject", "TLS: This is text!", "<email получателя>");  
    }               
}

