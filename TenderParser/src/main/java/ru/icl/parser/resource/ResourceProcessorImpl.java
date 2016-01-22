package ru.icl.parser.resource;

import ru.icl.parser.resource.ResourceProcessor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public static String urlSplit = "http://zakupki.gov.ru/epz/order/quicksearch/update.html?placeOfSearch=FZ_44&_placeOfSearch=on&placeOfSearch=FZ_223&_placeOfSearch=on&_placeOfSearch=on&priceFrom=0&priceTo=200+000+000+000&publishDateFrom=&publishDateTo=&updateDateFrom=&updateDateTo=&orderStages=AF&_orderStages=on&orderStages=CA&_orderStages=on&_orderStages=on&_orderStages=on&sortDirection=false&sortBy=UPDATE_DATE&recordsPerPage=_10&pageNo=&searchString=&strictEqual=false&morphology=false&showLotsInfo=false&isPaging=true&isHeaderClick=&checkIds=";    
    public List<Tender> process(StringBuilder httpResource) {
        
        String 
            url = null, //адрес сайта со список тендеров
            hrefFz44 = "http://zakupki.gov.ru",
            regexUrlFz223 = "http:.+?noticeId=(.\\d+)", //url для тендеров проходящих по фз223
            regexUrlFz44 = "/epz/.+?regNumber=(.\\d+)", //url для тендеров проходящих по фз44
//            regexId = "[0-9]{8,20}", //IdTender
            regexDateFz223 = "(Срок предоставления с).(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).((19|20)\\d\\d).(по).(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).((19|20)\\d\\d)",                
            regexDateFz44 = "(Дата и время окончания подачи заявок|Дата и время окончания подачи заявок \\(по местному времени\\)|Дата и время окончания подачи котировочных заявок).(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[012])\\.((19|20)\\d\\d)",
            keyword = MyController.keyword;     
 
        Integer 
            pageSum = null, //общее количество страниц          
            tenderSum = null; //общее количество тендеров 
        
        //вводится коллекция для хранения тендеров
        List<Tender> list = new ArrayList<Tender>();
        
        Matcher matcher = null;
        
        //запись ключевого слова
        Email email = new Email();        
        email.setKeyword(keyword);
        
        //поиск и вычисление общего количества страниц 
        Document docFirst =  Jsoup.parse(httpResource.toString());                
        Pattern pattern = Pattern.compile("\\d+");        
        Elements links = docFirst.select("div[class=allRecords]");          
        for (Element link : links) {
            matcher = pattern.matcher(link.text());  
        }        
//        try {            
            while(matcher.find()){ 
            //общее количество тендеров            
            tenderSum = Integer.valueOf(matcher.group());                                            
//        }
//        } catch (NullPointerException ex) {
//            System.out.println("На сайте zakupki.gov.ru регламентные работы");            
//            ex.printStackTrace();
        }   
        int modulo = tenderSum % 10;        
        //общее количество страниц        
        pageSum = modulo > 0 ? (tenderSum/10)+1 : (tenderSum/10);  
        System.out.println("Общее количество страниц " + pageSum);
        
        //Прохождение по всем страницам пока не будет достигнута последняя страница
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BeanFactory factory = (BeanFactory) context;
        ResourceProcessorImpl resBean = (ResourceProcessorImpl) factory.getBean("resourcebean"); 
//        HttpResourceImpl resimpl = new HttpResourceImpl();
        
        for(int pageList=1; pageList<pageSum+1; pageList++) {              
            url = resBean.getUrladdress().getUrlAddress(urlSplit, pageList, keyword);            
            System.out.println("URL " + url);
//            StringBuilder strBuild = new StringBuilder();
            StringBuilder strBuild = resBean.getHttpresource().getHttpResource(url);                         
            Document doc = Jsoup.parse(strBuild.toString()); 
            //Анализ страницы с тендерами             
            Elements elements = doc.select("td[class=descriptTenderTd]").select("dl").select("dt"); //link.text() -> № id_tender                                         
            int countTag = 0; 
//            System.out.println();
//            System.out.println("-----------------" + pageList + "-----------------");
            for (Element link : elements) {                  
                
                Tender tend = new Tender();                                 
                //Ключевое слово
                tend.setEmail(email);                
                // № id_Тендера
                tend.setIdTender(link.text());                
                // Название организации
                tend.setCompanyTender(doc.select("td[class=descriptTenderTd]").get(countTag).getElementsByTag("dd").get(0).text());                                               
                // Название тендера
                tend.setNameTender(doc.select("td[class=descriptTenderTd]").get(countTag).getElementsByTag("dd").get(1).text());                                                 
                //Стоимость
                tend.setCostTender(doc.select("div[class=registerBox]").get(countTag).getElementsByTag("td").get(2).text());                               
                countTag++;                             
                // ФЗ_223 - Поиск Крайнего срока тендера               
                Pattern patternFz223 = Pattern.compile(regexUrlFz223);
                Matcher matcherFz223 = patternFz223.matcher(link.html());        
                if (matcherFz223.find()) {                    
                    //url тендера 
                    tend.setUrlTender(matcherFz223.group());
                    Document docFz223 = null;
                    try {
                        //переходим на новую страницу где указан Крайний срок тендера
                        docFz223 = Jsoup.connect(matcherFz223.group()).timeout(30000).get();                                                 
                        //Deadline: <с dd.mm.yyyy по dd.mm.yyyy>
                        Pattern patternDate = Pattern.compile(regexDateFz223);
//                        Matcher matcherDate = patternDate.matcher(docFz223.select("div[class=noticeTabBoxWrapper]").get(5).text());//3
                        Matcher matcherDate = patternDate.matcher(docFz223.select("div[class=noticeTabBoxWrapper]").text());//3
                        StringBuilder strDate = new StringBuilder();
                        while (matcherDate.find()) {
                            strDate.append(matcherDate.group() + " ");
                        }
//                        tend.setDeadlineTender(strDate.toString()); 
                        if (strDate.toString().isEmpty()) {tend.setDeadlineTender("НЕТ ДАННЫХ"); }
                        else tend.setDeadlineTender(strDate.toString()); 
                        
                    } catch (Exception ex) {                        
                        Logger.getLogger(ResourceProcessorImpl.class.getName()).log(Level.SEVERE, null, ex);
                        tend.setDeadlineTender("Крайний срок тендера не удалось найти"); 
                        System.out.println("Крайний срок тендера не удалось найти");
                    }    
                }
                
                // ФЗ_44 - Поиск Крайнего срока тендера                                
                Pattern patternFz44 = Pattern.compile(regexUrlFz44);
                Matcher matcherFz44 = patternFz44.matcher(link.html());                
                if (matcherFz44.find()) {                                        
                    //url тендера 
                    tend.setUrlTender(hrefFz44.concat(matcherFz44.group()));                     
                    Document docFz44 = null;
                    try {
                        //переходим на новую страницу где указан Крайний срок тендера
                        docFz44 = Jsoup.connect(hrefFz44.concat(matcherFz44.group())).timeout(30000).get();                        
                        Pattern patternDate = Pattern.compile(regexDateFz44);
//                        Matcher matcherDate = patternDate.matcher(docFz44.select("table").get(3).text());
                        Matcher matcherDate = patternDate.matcher(docFz44.select("table").text());
                        
                        StringBuilder strDate = new StringBuilder();
                        while (matcherDate.find()) {
                            strDate.append(matcherDate.group() + " ");
                        }                        
                        if (strDate.toString().isEmpty()) {
                            tend.setDeadlineTender("НЕТ ДАННЫХ"); 
                        }
                        else tend.setDeadlineTender(strDate.toString()); 
                        
                    } catch (Exception ex) {
                        Logger.getLogger(ResourceProcessorImpl.class.getName()).log(Level.SEVERE, null, ex);
                        tend.setDeadlineTender("Крайний срок тендера не удалось найти"); 
                        System.out.println("Крайний срок тендера не удалось найти");
                    }                                 
                }               
                list.add(tend);
            }            
        } 
        return list;       
    }    
}    

