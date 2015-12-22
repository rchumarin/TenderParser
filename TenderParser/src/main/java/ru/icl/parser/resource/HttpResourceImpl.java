package ru.icl.parser.resource;

import ru.icl.parser.resource.HttpResource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

//получание страницы
public class HttpResourceImpl implements HttpResource {

    @Override
    public StringBuilder getHttpResource(String address) {
        
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(address);
        HttpResponse resp;
        BufferedReader br = null;
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();  
        try {
            resp = httpclient.execute(httpget);            
            int respCode = resp.getStatusLine().getStatusCode();          
            br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));                                    
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line + "\n"); 
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
        return stringBuilder;        
    }
    
}
