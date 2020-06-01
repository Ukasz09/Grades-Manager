package com.github.ukasz09.app;

import java.io.PrintStream;

public class Logger {
    public static PrintStream consoleOut = System.out;
    public static PrintStream consoleErr = System.err;

    //----------------------------------------------------------------------------------------------------------------//
    public static void logError(Class c, Exception e, PrintStream err) {
        err.println("Error in class: " + c + ", msg: " + e.getMessage());
    }

    public static void logMsg(String msg, PrintStream out) {
        out.println(msg);
    }

}
