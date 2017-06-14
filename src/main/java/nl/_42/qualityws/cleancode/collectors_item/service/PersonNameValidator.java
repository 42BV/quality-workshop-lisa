package nl._42.qualityws.cleancode.collectors_item.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class PersonNameValidator {

    private static Pattern pattern = Pattern.compile("[a-zA-Z -\\.]+");

    public boolean verifyName(String name) {
        Matcher matcher = pattern.matcher(name);
        return  matcher.matches() &&
                Character.isUpperCase(name.charAt(0));
    }

}
