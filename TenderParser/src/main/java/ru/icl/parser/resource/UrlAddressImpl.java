package ru.icl.parser.resource;

import java.util.regex.Pattern;

public class UrlAddressImpl implements UrlAddress {

    public String getUrlAddress(String urlSplit, int pageList, String keyword) {
        String[] arrayString = null;  
        Pattern PATTERN = Pattern.compile("(&pageNo=|&searchString=)");                                      
        arrayString = PATTERN.split(urlSplit); 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(arrayString[0]);
        stringBuilder.append("&pageNo=");
        stringBuilder.append(pageList);
        stringBuilder.append("&searchString=");
        stringBuilder.append(keyword);
        stringBuilder.append(arrayString[2]); 
        
        return stringBuilder.toString();
    }
    
}
