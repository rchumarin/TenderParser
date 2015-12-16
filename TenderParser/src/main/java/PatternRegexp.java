import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternRegexp {
    
    // поиск и выбор E-mail
    static final String email = "(\\w+)@(\\w+\\.)(\\w+)(\\.\\w+)*";   
    public static final Pattern pattern = Pattern.compile(email); 

           
    public static void doMatch(String word) {
//        Matcher matcher = pattern.matcher(word);
        Matcher matcher = pattern.matcher(word);
        while (matcher.find()) {
            System.out.println("E-mail организатора аукциона: " + matcher.group());
        }
    }
}
