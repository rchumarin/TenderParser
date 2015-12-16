import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import org.apache.commons.lang3.text.StrBuilder;

public class Main_ {
        
    public static void main(String[] args) {
        Document doc = null;         
        Integer pageList = 1, pageSum=null;                
        Matcher matcher = null;        
//        String publishDateFrom= ""; //"13.12.2015"
        String keyword = "радар";
        String url = "http://zakupki.gov.ru/epz/order/quicksearch/update.html?"
            + "placeOfSearch=FZ_44&_placeOfSearch=on&placeOfSearch=FZ_223&_placeOfSearch=on&placeOfSearch=FZ_94&_placeOfSearch=on"
                + "&priceFrom=500&priceTo=200+000+000+000"
                + "&publishDateFrom=&publishDateTo="
                + "&updateDateFrom=&updateDateTo="
                + "&orderStages=AF&_orderStages=on&orderStages=CA&_orderStages=on&_orderStages=on&_orderStages=on"
                + "&sortDirection=false&sortBy=UPDATE_DATE"
                + "&recordsPerPage=_10&pageNo=" + Integer.toString(pageList)
                + "&searchString=" + keyword 
                + "&strictEqual=false&morphology=false&showLotsInfo=false&isPaging=false&isHeaderClick=&checkIds=";                        
        
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse resp;
        BufferedReader br = null;
        String line = null;
        StrBuilder strBuilder = new StrBuilder();
        
        try {
            resp = httpclient.execute(httpget);
            int respCode = resp.getStatusLine().getStatusCode();          
            br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));                        
            while ((line = br.readLine()) != null) {
                strBuilder.append(line + "\n"); 
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }            
        doc =  Jsoup.parse(strBuilder.toString());          
        
        System.out.println("Вывод всех найденнных тендеров:");
        Elements elements = doc.select("div[class=registerBox]");  
        //td[class=amountTenderTd] td[class=descriptTenderTd]
        //div[id=exceedSphinxPageSizeDiv]
        //<div[class=registerBox]        
        for (Element element : elements) {
            System.out.println(element.text());
//            System.out.println(element.children().toString());
        }    
        System.out.println();
        System.out.println("1й найденный тендер:");
        Element element = elements.get(0);        
        System.out.println(element.text());
        System.out.println();
        
//        for(int i=0; i<10; i++) {
//            Element element = elements.get(i);        
//            PatternRegexp.doMatch(element.text());
//        }   

                
        //поиск тега <div class="allRecords">
        //определение общего количества всех тендеров по ключевому слову 
        //определение количества переходов на следующую страницу с тендерами (page-list),        
        //Regex - любое число
        Pattern pattern = Pattern.compile("\\d+");                
        Elements links = doc.select("div[class=allRecords]");        
        for (Element link : links) {
//            System.out.println("text : " + link.text());
            matcher = pattern.matcher(link.text());  
        }
        while(matcher.find()){                    
            pageSum = Integer.valueOf(matcher.group());                                
        }        
        System.out.println("Всего записей: " + pageSum);        
        int modulo = pageSum % 10;         
        pageList = modulo > 0 ? (pageSum/10)+1 : (pageSum/10);             
        System.out.println("Количество page-list: " + pageList);

/*
        //определение общего количества всех тендеров по ключевому слову 
        //определение количества переходов на следующую страницу с тендерами (page-list)
        Elements elements = doc.body().getElementsContainingOwnText("Всего");   
//        System.out.println(elements);
//        Elements elements = element.getElementsContainingOwnText("Всего"); 
        System.out.println(elements);    
        Iterator<Element> iterator = elements.iterator();        
        while(iterator.hasNext()) {
            String stringPageSum = iterator.next().childNodes().iterator().next().toString();            
            System.out.println(stringPageSum);                 
            matcher = pattern.matcher(stringPageSum);                                
        }            
        while(matcher.find()){                    
            pageSum = Integer.valueOf(matcher.group());                                
        }        
        System.out.println(pageSum);        
        int modulo = pageSum % 50;        
        pageNumber = modulo > 0 ? (pageSum/50)+1 : (pageSum/50);             
        System.out.println(pageNumber);
*/        
    }
    
}
