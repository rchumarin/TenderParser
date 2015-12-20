package ru.icl.parser.main;

import ru.icl.parser.factory.ResourceProcessorImpl;
import ru.icl.parser.factory.Factory;
import java.io.IOException;
import java.util.List;

//import ru.icl.parser.sendmail.*;

public class Main {
        
    public static void main(String[] args) throws IOException {         
                
        Factory factory = Factory.getInstance();
        StringBuilder httpResource = factory.getHttpResource().getHttpResource(ResourceProcessorImpl.url);
        List tenders = factory.getResourceProcessor().process(httpResource);
        
//        SenderTls tlsSender = new SenderTls("<ваш email на gmail>", "<ваш пароль>");
//        SenderSsl sslSender = new SenderSsl("<ваш email на gmail>", "<ваш пароль>");        
//        tlsSender.send("This is Subject", "TLS: This is text!", "support@", "<email получателя>");
//        sslSender.send("This is Subject", "SSL: This is text!", "support@", "<email получателя>");
   
    }               
}

