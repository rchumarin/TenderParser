
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    }
    
}
