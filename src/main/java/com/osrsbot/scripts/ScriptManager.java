package com.osrsbot.scripts;

import com.osrsbot.debug.DebugManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Manages loading, starting, and stopping of scripts.
 */
/**
 * Manages loading, starting, stopping, and restarting of scripts.
 * Tracks script states and allows for robust lifecycle management.
 */
public class ScriptManager {
    private static final List<Script> scripts = new ArrayList<>();
    private static final java.util.Map<Script, ScriptState> scriptStates = new java.util.HashMap<>();
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void register(Script script) {
        scripts.add(script);
        DebugManager.logInfo("Script registered: " + script.getName());
    }

    /**
     * Dynamically loads all Script classes from the "scripts" directory.
     * Only classes implementing Script and having a no-arg constructor are loaded.
     */
    public static void loadScriptsFromDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            DebugManager.logWarn("Script directory not found: " + dirPath);
            return;
        }
        try {
            URL[] urls = {dir.toURI().toURL()};
            try (URLClassLoader loader = new URLClassLoader(urls, Script.class.getClassLoader())) {
                for (File file : dir.listFiles((d, name) -> name.endsWith(".class"))) {
                    String className = file.getName().replace(".class", "");
                    try {
                        Class<?> cls = loader.loadClass(className);
                        if (Script.class.isAssignableFrom(cls)) {
                            Script script = (Script) cls.getDeclaredConstructor().newInstance();
                            register(script);
                        }
                    } catch (Exception e) {
                        DebugManager.logWarn("Failed to load script: " + className);
                        DebugManager.logException(e);
                    }
                }
            }
        } catch (Exception e) {
            DebugManager.logException(e);
        }
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
            // Publish event
            com.osrsbot.events.EventBus.publish(new com.osrsbot.events.events.ScriptStartedEvent(script));
        } catch (Exception e) {
            DebugManager.logException(e);
        }
    }

    public static void stop(Script script) {
        try {
            script.onStop();
            com.osrsbot.events.EventBus.publish(new com.osrsbot.events.events.ScriptStoppedEvent(script));
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