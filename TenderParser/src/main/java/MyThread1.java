
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.text.StrBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MyThread1 implements Runnable {
    Thread thrd;
    Document doc;
    StrBuilder strBuilder;
    Matcher matcher;
    Integer pageList, pageSum; 
	
    MyThread1(String name, StrBuilder strBuilder) {
            thrd = new Thread(this, name);
            this.strBuilder = strBuilder;
            thrd.start();
    }
        
    @Override
    public void run() {   
        System.out.println(thrd.getName() + " started");
        //поиск тега <div class="allRecords">
        //определение общего количества всех тендеров по ключевому слову 
        //определение количества переходов на следующую страницу с тендерами (page-list),                        
        doc =  Jsoup.parse(strBuilder.toString());         
        Pattern pattern = Pattern.compile("\\d+"); //Regex - любое число                
        Elements links = doc.select("div[class=allRecords]");        
        for (Element link : links) {
            matcher = pattern.matcher(link.text());  
        }
        while(matcher.find()){                    
            pageSum = Integer.valueOf(matcher.group());                                
        }        
        System.out.println("Всего записей: " + pageSum);        
        int modulo = pageSum % 10;         
        pageList = modulo > 0 ? (pageSum/10)+1 : (pageSum/10);             
        System.out.println("Количество page-list: " + pageList);
        System.out.println();
    }
    
}
