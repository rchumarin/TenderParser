package ru.icl.parser.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.icl.parser.model.Tender;
import ru.icl.parser.service.EmailService;
import ru.icl.parser.service.TenderService;

//@WebServlet(name = "View", urlPatterns = {"/view"})
public class View extends HttpServlet { 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {        
        ApplicationContext context = new ClassPathXmlApplicationContext("springContext.xml");
        TenderService serviceTender = (TenderService) context.getBean("tenderService");
        EmailService serviceEmail = (EmailService) context.getBean("emailService");
        List<Tender> tenderList = serviceTender.getAll();
        request.setAttribute("tend", tenderList);
        try {
            request.getRequestDispatcher("view.jsp").forward(request, response);
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}