package com.osrsbot.modules;

import com.osrsbot.debug.DebugManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Central module/plugin manager for registering and controlling modules.
 */
public class ModuleManager {
    private static final List<BotModule> modules = new ArrayList<>();

    public static void register(BotModule module) {
        modules.add(module);
        DebugManager.logInfo("Module registered: " + module.getName());
    }

    public static void startAll() {
        for (BotModule module : modules) {
            try {
                module.onStart();
            } catch (Exception e) {
                DebugManager.logException(e);
            }
        }
    }

    public static void stopAll() {
        for (BotModule module : modules) {
            try {
                module.onStop();
            } catch (Exception e) {
                DebugManager.logException(e);
            }
        }
    }

    public static List<BotModule> getModules() {
        return Collections.unmodifiableList(modules);
    }
}