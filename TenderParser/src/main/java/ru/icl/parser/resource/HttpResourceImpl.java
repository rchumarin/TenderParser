package ru.icl.parser.resource;

import ru.icl.parser.resource.HttpResource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

//получание страницы
public class HttpResourceImpl implements HttpResource {

    public StringBuilder getHttpResource(String address) {
        HttpClient httpclient = new DefaultHttpClient();        
        HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 30000); // Timeout Limit        
        HttpGet httpget = new HttpGet(address);
        HttpResponse resp;
        BufferedReader br = null;
        String line = null;
        StringBuilder stringBuilder = null;
        stringBuilder = new StringBuilder();  
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
        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown();        
        return stringBuilder;       
    }   
}
