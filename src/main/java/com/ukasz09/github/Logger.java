package com.ukasz09.github;

public class Logger {
    public static void logError(Class c, Exception e) {
        System.err.println("Error in class: " + c + ", msg: " + e.getMessage());
    }
}
