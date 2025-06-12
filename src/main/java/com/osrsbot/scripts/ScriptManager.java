package com.osrsbot.scripts;

import com.osrsbot.debug.DebugManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages loading, starting, and stopping of scripts.
 */
public class ScriptManager {
    private static final List<Script> scripts = new ArrayList<>();
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void register(Script script) {
        scripts.add(script);
        DebugManager.logInfo("Script registered: " + script.getName());
    }

    public static void startAll() {
        for (Script script : scripts) {
            start(script);
        }
    }

    public static void start(Script script) {
        try {
            script.onStart();
            executor.submit(script);
        } catch (Exception e) {
            DebugManager.logException(e);
        }
    }

    public static void stop(Script script) {
        try {
            script.onStop();
        } catch (Exception e) {
            DebugManager.logException(e);
        }
    }

    public static void stopAll() {
        for (Script script : scripts) {
            stop(script);
        }
        executor.shutdownNow();
    }

    public static List<Script> getScripts() {
        return Collections.unmodifiableList(scripts);
    }

    /**
     * Dynamically loads and registers scripts from the ./scripts directory (expects .class files).
     */
    public static void loadScriptsFromDirectory() {
        try {
            java.io.File dir = new java.io.File("./scripts");
            if (!dir.exists() || !dir.isDirectory()) {
                DebugManager.logInfo("No scripts directory found for dynamic loading.");
                return;
            }
            java.net.URL url = dir.toURI().toURL();
            ClassLoader loader = new java.net.URLClassLoader(new java.net.URL[]{url}, ScriptManager.class.getClassLoader());

            for (String file : dir.list()) {
                if (file.endsWith(".class")) {
                    String className = file.substring(0, file.length() - 6);
                    try {
                        Class<?> clazz = loader.loadClass(className);
                        if (Script.class.isAssignableFrom(clazz)) {
                            Script script = (Script) clazz.getDeclaredConstructor().newInstance();
                            register(script);
                        }
                    } catch (Exception e) {
                        DebugManager.logException(e);
                    }
                }
            }
        } catch (Exception e) {
            DebugManager.logException(e);
        }
    }
}