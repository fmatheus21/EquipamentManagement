package com.mycompany.myapp.util;

/**
 *
 * @author fmatheus
 */
public class AppUtil {

    public static String removeDuplicateSpace(String string) {
        return string.replaceAll("\\s+", " ").trim();
    }

}
