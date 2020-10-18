package Util;

public class StringUtil {

    public static String removeEscapedQuotes(String s) {
        return s.replaceAll("\"", "");
    }
}
