package com.osrsbot.debug;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Centralized debug and logging manager.
 * Supports log levels, event tracing, error reporting, and runtime hooks.
 */
public class DebugManager {
    public enum Level { DEBUG, INFO, WARN, ERROR }

    private static Level currentLevel = Level.DEBUG;
    private static boolean traceApiCalls = true;
    private static PrintWriter fileLogger = null;
    private static volatile String lastCriticalError = null;

    static {
        try {
            fileLogger = new PrintWriter(new FileWriter("osrsbot.log", true), true);
        } catch (Exception ignored) {}
    }

    public static void surfaceCriticalError(String msg) {
        lastCriticalError = msg;
        System.err.println("[FATAL] " + msg);
        com.osrsbot.gui.OverlayManager.showInfo("[FATAL] " + msg);
    }

    public static String getLastCriticalError() {
        return lastCriticalError;
    }

    public static void shutdown() {
        if (fileLogger != null) {
            fileLogger.flush();
            fileLogger.close();
        }
    }

    public static void setLevel(Level level) {
        currentLevel = level;
    }

    public static void enableApiCallTracing(boolean enable) {
        traceApiCalls = enable;
    }

    public static void logDebug(String msg) {
        log(Level.DEBUG, msg);
    }

    public static void logInfo(String msg) {
        log(Level.INFO, msg);
    }

    public static void logWarn(String msg) {
        log(Level.WARN, msg);
    }

    public static void logError(String msg) {
        log(Level.ERROR, msg);
    }

    public static void logApiCall(String call) {
        if (traceApiCalls) {
            log(Level.DEBUG, "[API] " + call);
        }
    }

    public static void logException(Throwable t) {
        log(Level.ERROR, "Exception: " + t.getClass().getSimpleName() + " - " + t.getMessage());
        for (StackTraceElement e : t.getStackTrace()) {
            System.err.println("\tat " + e);
        }
    }

    private static void log(Level level, String msg) {
        if (level.ordinal() >= currentLevel.ordinal()) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            System.out.println("[" + time + "] [" + level + "] " + msg);
        }
    }
}