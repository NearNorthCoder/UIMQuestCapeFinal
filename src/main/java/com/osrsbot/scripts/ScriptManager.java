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
            try {
                script.onStart();
                executor.submit(script);
            } catch (Exception e) {
                DebugManager.logException(e);
            }
        }
    }

    public static void stopAll() {
        for (Script script : scripts) {
            try {
                script.onStop();
            } catch (Exception e) {
                DebugManager.logException(e);
            }
        }
        executor.shutdownNow();
    }

    public static List<Script> getScripts() {
        return Collections.unmodifiableList(scripts);
    }
}