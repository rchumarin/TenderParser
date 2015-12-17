import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternRegexp {
    
    static String email = "(\\w+)@(\\w+\\.)(\\w+)(\\.\\w+)*";
    static String href = "http://zakupki.gov.ru";
    static String urlFz223 = "http:.+?noticeId=(.\\d+)"; //для url тендеров проходящих по фз223
    static String urlFz44 = "/epz/.+?regNumber=(.\\d+)"; //для url тендеров проходящих по фз44
    
    static Pattern patternFz223 = Pattern.compile(urlFz223);
    static Pattern patternFz44 = Pattern.compile(urlFz44);
       
    public static void doMatch(String word) {         
        
        Matcher matcherFz223 = patternFz223.matcher(word);        
        if (matcherFz223.find()) {
            System.out.println(matcherFz223.group()); 
        }
        
        Matcher matcherFz44 = patternFz44.matcher(word);
        if (matcherFz44.find()) {
            System.out.println(href.concat(matcherFz44.group())); 
        }  
        

        
    }
}
