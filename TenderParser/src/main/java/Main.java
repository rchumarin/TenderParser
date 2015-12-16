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

public class Main {
        
    public static void main(String[] args) {        
        Document doc = null;         
        Integer pageList = 1, pageSum=null;                
        Matcher matcher = null;        
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
        
        MyThread1 mt1 = new MyThread1("MyThread1", strBuilder);
        MyThread2 mt2 = new MyThread2("MyThread2", strBuilder);		
        try {
                mt1.thrd.join();
                mt2.thrd.join();
        }
        catch(InterruptedException exc) {
                System.out.println("Main thread interrupted.");
        }
        
    }
    
}
