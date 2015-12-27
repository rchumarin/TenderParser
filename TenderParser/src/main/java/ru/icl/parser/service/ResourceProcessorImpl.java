package ru.icl.parser.service;

import ru.icl.parser.service.ResourceProcessor;
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
import ru.icl.parser.model.Tender;

//представляет найденные тендеры
public class ResourceProcessorImpl implements ResourceProcessor{   
    
    static Integer pageList = 1;
    static String keyword = "cisco";        
    static String urlBegin = "http://zakupki.gov.ru/epz/order/quicksearch/update.html?placeOfSearch=FZ_44&_placeOfSearch=on&placeOfSearch=FZ_223&_placeOfSearch=on&_placeOfSearch=on&priceFrom=0&priceTo=200+000+000+000&publishDateFrom=&publishDateTo=&updateDateFrom=&updateDateTo=&orderStages=AF&_orderStages=on&orderStages=CA&_orderStages=on&_orderStages=on&_orderStages=on&sortDirection=false&sortBy=UPDATE_DATE&recordsPerPage=_10&pageNo=";        
    static String urlEnd = "&strictEqual=false&morphology=false&showLotsInfo=false&isPaging=true&isHeaderClick=&checkIds=";                           
    public final static String url = urlBegin.concat(Integer.toString(pageList)).concat("&searchString=").concat(keyword).concat(urlEnd);                
    
    String hrefFz44 = "http://zakupki.gov.ru";
    
    String regexUrlFz223 = "http:.+?noticeId=(.\\d+)"; //url для тендеров проходящих по фз223
    String regexUrlFz44 = "/epz/.+?regNumber=(.\\d+)"; //url для тендеров проходящих по фз44
    String regexId = "[0-9]{8,20}"; //IdTender
    String regexDateFz223 ="[^w]..(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[012])\\.((19|20)\\d\\d)";
    String regexDateFz44 = "(Дата и время окончания подачи заявок|Дата и время окончания подачи заявок \\(по местному времени\\)|Дата и время окончания подачи котировочных заявок).(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[012])\\.((19|20)\\d\\d)";
            
    @Override
    public List process(StringBuilder httpResource) {
     
        Matcher matcher = null;
        Integer pageList, pageSum = null; 
        List<Tender> list = new ArrayList<Tender>();
        
        //поиск и вычисление общего количества страниц                        
        Document doc =  Jsoup.parse(httpResource.toString());         
        Pattern pattern = Pattern.compile("\\d+");        
        Elements links = doc.select("div[class=allRecords]");        
        for (Element link : links) {
            matcher = pattern.matcher(link.text());  
        }
        while(matcher.find()){                    
            pageSum = Integer.valueOf(matcher.group());                                
        }                
        int modulo = pageSum % 10;         
        pageList = modulo > 0 ? (pageSum/10)+1 : (pageSum/10); //общее количество страниц

        //Прохождение по всем страницам пока не будет достигнута последняя страница
        for(int i=1; i<=pageList; i++) {    
            
            //Анализ страницы с тендерами             
            Elements elements = doc.select("td[class=descriptTenderTd]").select("dl").select("dt"); //link.text() -> № id_tender                             
            Tender tend = new Tender();            
            int countTag = 0;            
            for (Element link : elements) {                
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
                    try {
                        //переходим на новую страницу где указан Крайний срок тендера
                        Document docFz223 = Jsoup.connect(matcherFz223.group()).timeout(10000).get();                         
                        //Deadline: <с dd.mm.yyyy по dd.mm.yyyy>
                        Pattern patternDate = Pattern.compile(regexDateFz223);
                        Matcher matcherDate = patternDate.matcher(docFz223.select("div[class=noticeTabBoxWrapper]").get(5).text());
                        StringBuilder strDate = new StringBuilder();
                        while (matcherDate.find()) {
                            strDate.append(matcherDate.group() + " ");
                        }
                        tend.setDeadlineTender(strDate.toString());    
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }    
                }
                
                // ФЗ_44 - Поиск Крайнего срока тендера
                Pattern patternFz44 = Pattern.compile(regexUrlFz44);
                Matcher matcherFz44 = patternFz44.matcher(link.html());
                if (matcherFz44.find()) {                                              
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
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } 
                    
                list.add(tend);
                }             
            }            
        }
        return list;            
    }   
}
