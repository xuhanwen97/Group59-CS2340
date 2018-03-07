package com.example.xu.group59.Utils;

public class StringUtils {
    // Util function to determine if string is empty or null
    public static boolean isNullOrEmpty(String input) {
        if (input == null || input.trim().isEmpty()) {
            return true;
        }

        return false;
    }

}
