
package ru.icl.parser.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
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
import ru.icl.parser.model.Email;
import ru.icl.parser.model.Tender;
import static ru.icl.parser.resource.ResourceProcessorImpl.urlSplit;

public class ResourceProcessorCallable implements Callable<List<Tender>>{
    
    private int name;    
    
    public String             
        hrefFz44 = "http://zakupki.gov.ru",
        regexUrlFz223 = "http:.+?noticeId=(.\\d+)", //url для тендеров проходящих по фз223
        regexUrlFz44 = "/epz/.+?regNumber=(.\\d+)", //url для тендеров проходящих по фз44
//            regexId = "[0-9]{8,20}", //IdTender
        regexDateFz223 = "(Срок предоставления с).(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).((19|20)\\d\\d).(по).(0?[1-9]|[12][0-9]|3[01]).(0?[1-9]|1[012]).((19|20)\\d\\d)",                
        regexDateFz44 = "(Дата и время окончания подачи заявок|Дата и время окончания подачи заявок \\(по местному времени\\)|Дата и время окончания подачи котировочных заявок).(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[012])\\.((19|20)\\d\\d)";    
    
    private List<Tender> list = new ArrayList<Tender>();
    
    private StringBuilder strBuild; 
    private Email email;
            
    public ResourceProcessorCallable(StringBuilder strBuild, Email email, int name) {        
        this.strBuild = strBuild;
        this.email = email;
        this.name = name;
    }
    
    public List<Tender> call() throws Exception {       
        
        Matcher matcher = null;                
                        
        Document doc = Jsoup.parse(strBuild.toString()); 
        //Анализ страницы с тендерами             
        Elements elements = doc.select("td[class=descriptTenderTd]").select("dl").select("dt"); //link.text() -> № id_tender                                         
        int countTag = 0; 
        System.out.println();        
        for (Element link : elements) {                  

            Tender tend = new Tender();                                 
            //Ключевое слово
            tend.setEmail(email);                
            // № id_Тендера
            tend.setIdTender(link.text());
            
            System.out.println("=====================");
            System.out.println("Поток " + name);
            System.out.println(link.text());
            
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
                    Matcher matcherDate = patternDate.matcher(docFz223.select("div[class=noticeTabBoxWrapper]").text());//3
                    StringBuilder strDate = new StringBuilder();
                    while (matcherDate.find()) {
                        strDate.append(matcherDate.group() + " ");
                    }
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
            System.out.println("=====================");
            list.add(tend);
            Thread.yield();
        }
    return list;  
    } 
     
}
