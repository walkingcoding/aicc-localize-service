package com.walkingcoding.aicc.processor.util;

/**
 * @author songhuiqing
 */
public class PrintColorUtils {

    public static String printFontRed(String input) {
        return printColor(input, "31");
    }

    public static String printFontBlue(String input) {
        return printColor(input, "32");
    }

    public static String printFontYellow(String input) {
        return printColor(input, "33");
    }

    public static String printFontGreen(String input) {
        return printColor(input, "34");
    }


    private static String printColor(String input, String num) {
        return String.format("\u001b[%sm %s \u001b[0m", num, input);
    }
}
