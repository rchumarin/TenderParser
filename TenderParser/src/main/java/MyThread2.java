import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MyThread2 implements Runnable{
    Thread thrd;
    Document doc = null;
    StrBuilder strBuilder;
	
    MyThread2(String name, StrBuilder strBuilder) {
            thrd = new Thread(this, name);
            this.strBuilder = strBuilder;
            thrd.start();
    }
        
    @Override
    public void run() { 
        System.out.println(thrd.getName() + " started");
        doc =  Jsoup.parse(strBuilder.toString());                  
        Elements elements = doc.select("td[class=descriptTenderTd]").select("dl").select("dt");

        System.out.println("\nLinks: " + elements.size());
        for (Element link : elements) {
            PatternRegexp.doMatch(link.html());
        }
        
        System.out.println();
        Elements elements2 = doc.select("div[class=registerBox]");                   
        for (Element element : elements2) {
            System.out.println(element.text());               
        } 
        
//        System.out.println();
//        System.out.println("3й найденный тендер:");
//        Element element = elements.get(2);        
//        System.out.println(element.text());
//        System.out.println();
//                      
//        Pattern pattern = Pattern.compile("[0-9]{8,20}"); //номер заявки тендера                                
//        Matcher matcher = pattern.matcher(element.text());          
//        if(matcher.find()){                    
//            System.out.println("ID тендера: " + matcher.group());                                
//        }        
        
        /*
        N
        ID тендера
        Название организации
        Название тендера
        Стоимость лота
        Срок подачи заявки
        Ключевое слово
        */
        
        
//        for(int i=0; i<10; i++) {
//            Element element = elements.get(i);        
//            PatternRegexp.doMatch(element.text());
//        }   
    }
}
