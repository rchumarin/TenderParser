package ru.icl.parser.resource;

import ru.icl.parser.resource.ResourceProcessor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.icl.parser.controller.MyController;
import ru.icl.parser.dao.TenderDao;
import ru.icl.parser.model.Email;
import ru.icl.parser.model.Tender;

//представляет найденные тендеры
public class ResourceProcessorImpl implements ResourceProcessor{

    private HttpResource httpresource;
    private UrlAddress urladdress;
    
    public HttpResource getHttpresource() {
        return httpresource;
    }
    public void setHttpresource(HttpResource httpresource) {
        this.httpresource = httpresource;
    }

    public UrlAddress getUrladdress() {
        return urladdress;
    }
    public void setUrladdress(UrlAddress urladdress) {
        this.urladdress = urladdress;
    }
    
    //общий адрес к сайту тендера
    public static String urlSplit = "http://zakupki.gov.ru/epz/order/quicksearch/update.html?placeOfSearch=FZ_44&_placeOfSearch=on&placeOfSearch=FZ_223&_placeOfSearch=on&_placeOfSearch=on&priceFrom=0&priceTo=200+000+000+000&publishDateFrom=&publishDateTo=&updateDateFrom=&updateDateTo=&orderStages=AF&_orderStages=on&orderStages=CA&_orderStages=on&_orderStages=on&_orderStages=on&sortDirection=false&sortBy=UPDATE_DATE&recordsPerPage=_10&pageNo=1&searchString=&strictEqual=false&morphology=false&showLotsInfo=false&isPaging=true&isHeaderClick=&checkIds=";    
    public List<Tender> process(StringBuilder httpResource) {
        
        String 
            url = null, //адрес сайта со список тендеров
            keyword = MyController.keyword;     
        Integer 
            pageSum = null, //общее количество страниц          
            tenderSum = null; //общее количество тендеров 
        
        //вводится коллекция для хранения тендеров
        List<Tender> list = new ArrayList<Tender>();
        
        Matcher matcher = null;
        
//        запись ключевого слова
        Email email = new Email();        
        email.setKeyword(keyword);
        
        //поиск и вычисление общего количества страниц 
        Document docFirst =  Jsoup.parse(httpResource.toString());                
        Pattern pattern = Pattern.compile("\\d+");        
        Elements links = docFirst.select("div[class=allRecords]");          
        for (Element link : links) {
            matcher = pattern.matcher(link.text());  
        }        
           
        while(matcher.find()){ 
            //общее количество тендеров            
            tenderSum = Integer.valueOf(matcher.group());                                            
        }   
        int modulo = tenderSum % 10;        
        //общее количество страниц        
        pageSum = modulo > 0 ? (tenderSum/10)+1 : (tenderSum/10);  
        System.out.println("Общее количество страниц " + pageSum);
        
        //Прохождение по всем страницам пока не будет достигнута последняя страница
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BeanFactory factory = (BeanFactory) context;
        ResourceProcessorImpl resBean = (ResourceProcessorImpl) factory.getBean("resourcebean"); 
        
        //создать ExecutorService на базе пула из n-потоков (n=pageSum)
        ExecutorService exec = Executors.newFixedThreadPool(pageSum);
        ArrayList<Future<List<Tender>>> results = new ArrayList<Future<List<Tender>>>();        
        for(int pageList=1; pageList<=pageSum; pageList++) {                      
            url = resBean.getUrladdress().getUrlAddress(urlSplit, pageList, keyword);            
            System.out.println("URL " + url);
            StringBuilder strBuild = resBean.getHttpresource().getHttpResource(url); 
            //поместить задачу в очередь на выполнение
            results.add(exec.submit(new ResourceProcessorCallable(strBuild, email, pageList)));
        }            
        try {
            //получить результат выполнения задачи            
            for(Future<List<Tender>> futureList : results) {
                for(Tender tender : futureList.get()) {
                        list.add(tender);
                }
                System.out.println();				           
            }    
        } catch (InterruptedException ie) {           
            ie.printStackTrace(System.err);
        } catch (ExecutionException ee) {
            ee.printStackTrace(System.err);
        }
        finally {
            exec.shutdown();                       
        } 
        return list;                           
    }    
}