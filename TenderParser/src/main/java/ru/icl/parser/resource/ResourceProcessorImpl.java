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

//представляет найденные тендеры
public class ResourceProcessorImpl implements ResourceProcessor{
        
    static Integer pageList = 1;
    static String keyword = "cisco";        
    static String urlBegin = "http://zakupki.gov.ru/epz/order/quicksearch/update.html?placeOfSearch=FZ_44&_placeOfSearch=on&placeOfSearch=FZ_223&_placeOfSearch=on&_placeOfSearch=on&priceFrom=0&priceTo=200+000+000+000&publishDateFrom=&publishDateTo=&updateDateFrom=&updateDateTo=&orderStages=AF&_orderStages=on&orderStages=CA&_orderStages=on&_orderStages=on&_orderStages=on&sortDirection=false&sortBy=UPDATE_DATE&recordsPerPage=_10&pageNo=";        
    static String urlEnd = "&strictEqual=false&morphology=false&showLotsInfo=false&isPaging=true&isHeaderClick=&checkIds=";                   
    public final static String url = urlBegin.concat(Integer.toString(pageList)).concat("&searchString=").concat(keyword).concat(urlEnd);        
    
    String email = "(\\w+)@(\\w+\\.)(\\w+)(\\.\\w+)*";
    String hrefFz44 = "http://zakupki.gov.ru";
    String regexFz223 = "http:.+?noticeId=(.\\d+)"; //url для тендеров проходящих по фз223
    String regexFz44 = "/epz/.+?regNumber=(.\\d+)"; //url для тендеров проходящих по фз44
    String regexId = "[0-9]{8,20}"; //IdTender
    Pattern patternFz223 = Pattern.compile(regexFz223);
    Pattern patternFz44 = Pattern.compile(regexFz44);
        
    @Override
    public List process(StringBuilder httpResource) {
     
    Matcher matcher = null;
    Integer pageList, pageSum = null; 
    List<String> list = new ArrayList<>();

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
    pageList = modulo > 0 ? (pageSum/10)+1 : (pageSum/10);             

    //Анализ страницы с тендерами                 
    Elements elements = doc.select("td[class=descriptTenderTd]").select("dl").select("dt");
    for (Element link : elements) {           
//      ФЗ-223             
        Matcher matcherFz223 = patternFz223.matcher(link.html());        
        if (matcherFz223.find()) {
            list.add(matcherFz223.group());             
//            try {
//                Document docFz223 = Jsoup.connect(matcherFz223.group()).get();                                             
//                Elements elementsFz223 = doc.select("table").select("tr");                       
//                elementsFz223.get(2).child(1).text()); //id tender
//                elementsFz223.get(15).child(1).text());//название организации
//                elementsFz223.get(4).child(1).text()); //Название тендера
//                elementsFz223.get(24).child(1).text());//dd.mm.yyyy h:m 
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }    
        }
//      ФЗ-44
        Matcher matcherFz44 = patternFz44.matcher(link.html());
        if (matcherFz44.find()) {            
            list.add(hrefFz44.concat(matcherFz44.group()));                
//            try {                
//                Document docFz44 = Jsoup.connect(hrefFz44.concat(matcherFz44.group())).get();
//                Elements elementsFz44 = docFz44.select("table").select("tr");               
//                // поиск IdTender 
//                Elements elementsId = docFz44.select("ul[class=breadcrumbs]"); 
//                Pattern pId = Pattern.compile(regexId);
//                Matcher mId = pId.matcher(elementsId.text());
//                while(mId.find()) System.out.print(mId.group()); //id tender              
//                elementsFz44.get(9).child(1).text()); //название организации
//                elementsFz44.get(5).child(1).text()); //Название тендера
//                elementsFz44.get(25).child(1).text()); //стоимость        
//                elementsFz44.get(18).child(1).text()); //dd.mm.yyyy h:m                
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }    
        }  
    }        
    return list;            
    }
    
}
