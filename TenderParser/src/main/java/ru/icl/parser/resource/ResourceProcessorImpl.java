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
import ru.icl.parser.dao.TenderDao;
import ru.icl.parser.model.Email;
import ru.icl.parser.model.Tender;

//представляет найденные тендеры
public class ResourceProcessorImpl implements ResourceProcessor{
    
    private HttpResource httpres;    
    public HttpResource getHttpres() {
        return httpres;
    }
    public void setHttpres(HttpResource httpres) {
        this.httpres = httpres;
    }       
    
    static Integer pageList = 1;
    static String keyword = "свеча";  
            
    static String urlBegin = "http://zakupki.gov.ru/epz/order/quicksearch/update.html?placeOfSearch=FZ_44&_placeOfSearch=on&placeOfSearch=FZ_223&_placeOfSearch=on&_placeOfSearch=on&priceFrom=0&priceTo=200+000+000+000&publishDateFrom=&publishDateTo=&updateDateFrom=&updateDateTo=&orderStages=AF&_orderStages=on&orderStages=CA&_orderStages=on&_orderStages=on&_orderStages=on&sortDirection=false&sortBy=UPDATE_DATE&recordsPerPage=_10&pageNo=";        
    static String urlEnd = "&strictEqual=false&morphology=false&showLotsInfo=false&isPaging=true&isHeaderClick=&checkIds=";                           
    public static String url = urlBegin.concat(Integer.toString(pageList)).concat("&searchString=").concat(keyword).concat(urlEnd);                
            
    @Override
    public List<Tender> process(StringBuilder httpResource) {
     
        String hrefFz44 = "http://zakupki.gov.ru";

        String regexUrlFz223 = "http:.+?noticeId=(.\\d+)"; //url для тендеров проходящих по фз223
        String regexUrlFz44 = "/epz/.+?regNumber=(.\\d+)"; //url для тендеров проходящих по фз44
        String regexId = "[0-9]{8,20}"; //IdTender
        String regexDateFz223 ="[^w]..(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[012])\\.((19|20)\\d\\d)";
        String regexDateFz44 = "(Дата и время окончания подачи заявок|Дата и время окончания подачи заявок \\(по местному времени\\)|Дата и время окончания подачи котировочных заявок).(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[012])\\.((19|20)\\d\\d)";        
        
        Matcher matcher = null;
        Integer pageSumm = null, pageSum = null;
        //вводится коллекция для хранения тендеров 
        List<Tender> list = new ArrayList<Tender>();   
        
        //запись ключевого слова
        Email email = new Email();        
        email.setKeyword(keyword);
        
        //поиск и вычисление общего количества страниц 
        Document doc =  Jsoup.parse(httpResource.toString());        
        Pattern pattern = Pattern.compile("\\d+");        
        Elements links = doc.select("div[class=allRecords]");        
        for (Element link : links) {
            matcher = pattern.matcher(link.text());  
        }
        try { 
            while(matcher.find()){ 
            //общее количество тендеров            
            pageSum = Integer.valueOf(matcher.group());                                            
        }
        } catch (NullPointerException ex) {
//            System.out.println("На сайте zakupki.gov.ru регламентные работы");            
            ex.printStackTrace();
        }   
        int modulo = pageSum % 10;         
        //общее количество страниц
        pageSumm = modulo > 0 ? (pageSum/10)+1 : (pageSum/10);         
        
        //Прохождение по всем страницам пока не будет достигнута последняя страница
        ApplicationContext context = new ClassPathXmlApplicationContext("springContext.xml");
        BeanFactory factory = (BeanFactory) context;
        ResourceProcessorImpl resBean = (ResourceProcessorImpl) factory.getBean("resourcebean");                                                    
        for(pageList=1; pageList<pageSumm+1; pageList++) {  
            url = urlBegin.concat(Integer.toString(pageList)).concat("&searchString=").concat(keyword).concat(urlEnd);      
            StringBuilder strBuild = resBean.getHttpres().getHttpResource(url);       
            doc =  Jsoup.parse(strBuild.toString());                        
            //Анализ страницы с тендерами             
            Elements elements = doc.select("td[class=descriptTenderTd]").select("dl").select("dt"); //link.text() -> № id_tender                                         
            int countTag = 0;            
            
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
                    try {
                        //переходим на новую страницу где указан Крайний срок тендера
                        Document docFz223 = Jsoup.connect(matcherFz223.group()).timeout(30000).get();                         
                        //Deadline: <с dd.mm.yyyy по dd.mm.yyyy>
                        Pattern patternDate = Pattern.compile(regexDateFz223);
                        Matcher matcherDate = patternDate.matcher(docFz223.select("div[class=noticeTabBoxWrapper]").get(5).text());
                        StringBuilder strDate = new StringBuilder();
                        while (matcherDate.find()) {
                            strDate.append(matcherDate.group() + " ");
                        }
                        tend.setDeadlineTender(strDate.toString());                         
                    } catch (Exception ex) {                        
                        tend.setDeadlineTender("Крайний срок заявки не удалось найти");                        
                        ex.printStackTrace();
                    }    
                }
                
                // ФЗ_44 - Поиск Крайнего срока тендера
                Pattern patternFz44 = Pattern.compile(regexUrlFz44);
                Matcher matcherFz44 = patternFz44.matcher(link.html());
                if (matcherFz44.find()) {                    
                    //url тендера 
                    tend.setUrlTender(hrefFz44.concat(matcherFz44.group()));                     
                    try {
                        //переходим на новую страницу где указан Крайний срок тендера
                        Document docFz44 = Jsoup.connect(hrefFz44.concat(matcherFz44.group())).timeout(30000).get();
                        Pattern patternDate = Pattern.compile(regexDateFz44);
                        Matcher matcherDate = patternDate.matcher(docFz44.select("table").get(3).text());
                        StringBuilder strDate = new StringBuilder();
                        while (matcherDate.find()) {
                            strDate.append(matcherDate.group() + " ");
                        }
                        tend.setDeadlineTender(strDate.toString());                         
                    } catch (Exception ex) {
                        tend.setDeadlineTender("Крайний срок заявки не удалось найти");                        
                        ex.printStackTrace();
                    }                                 
                }                                               
                list.add(tend);
            }                        
        }                                
        return list;       
    }    
}    

