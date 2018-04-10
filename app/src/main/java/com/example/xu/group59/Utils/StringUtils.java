package com.example.xu.group59.Utils;

/**
 * A class called String Utils that contains a method to check strings
 */
public class StringUtils {
    // Util function to determine if string is empty or null

    /**
     * A method that checks if a string is empty or null
     * @param input takes in a string of input
     * @return a boolean of true if the string is null or empty
     */
    public static boolean isNullOrEmpty(String input) {
        return (input == null) || input.trim().isEmpty();

    }

}
