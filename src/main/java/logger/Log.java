package logger;

import org.apache.log4j.Logger;

public class Log {
    public static void debug(String message) {
        Logger.getLogger(getCallingClassName()).debug(message);
    }

    public static void debug(String template, Object... args) {
        debug(String.format(template, args));
    }

    public static void debug(Object message) {
        debug(message.toString());
    }


    public static void info(String message) {
        Logger.getLogger(getCallingClassName()).info(message);
    }

    public static void info(String template, Object... args) {
        info(String.format(template, args));
    }

    public static void info(Object message) {
        info(message.toString());
    }


    public static void warn(String message) {
        Logger.getLogger(getCallingClassName()).warn(message);
    }

    public static void warn(String template, Object... args) {
        warn(String.format(template, args));
    }

    public static void warn(Object message) {
        warn(message.toString());
    }


    public static void error(String message) {
        Logger.getLogger(getCallingClassName()).error(message);
    }

    public static void error(String template, Object... args) {
        error(String.format(template, args));
    }

    public static void error(Object message) {
        error(message.toString());
    }

    private static String getCallingClassName() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        String fullClassName = stackTrace[2].getClassName();
        if (fullClassName.equals(Log.class.getName()) && stackTrace.length > 3) { //for overloaded methods
            fullClassName = stackTrace[3].getClassName();
        }
        return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
    }
}
