package utils;

import java.util.regex.Pattern;

public class Sanitize {
    private static final Pattern PATTERN = Pattern.compile("[a-zA-Z0-9\\s]{1,44}");

    public static String sanitize(String input) {
        if (input != null && PATTERN.matcher(input).matches()) {
            return null;
        }
        return "***";
    }
}
