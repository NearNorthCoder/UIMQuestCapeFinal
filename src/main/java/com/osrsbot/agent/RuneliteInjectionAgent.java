package com.osrsbot.agent;

import com.osrsbot.api.ApiManager;
import com.osrsbot.debug.DebugManager;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class RuneliteInjectionAgent {
    /**
     * This method is called before the main method of the target JVM (RuneLite) when using -javaagent.
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        DebugManager.logInfo("Agent loaded successfully! Attaching to RuneLite...");
        inst.addTransformer(new RuneliteClassTransformer(), true);
        DebugManager.logInfo("ApiManager and DebugManager initialized.");
    }

    /**
     * This method is called if the agent is attached to a running JVM.
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        DebugManager.logInfo("Agent attached at runtime!");
        inst.addTransformer(new RuneliteClassTransformer(), true);
    }

    /**
     * Transformer that detects when any RuneLite class is loaded and logs it, also triggers API connection.
     */
    private static class RuneliteClassTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(
                Module module,
                ClassLoader loader,
                String className,
                Class<?> classBeingRedefined,
                ProtectionDomain protectionDomain,
                byte[] classfileBuffer) {

            // Log loading of all RuneLite classes for debugging
            if (className != null && className.startsWith("net/runelite/")) {
                DebugManager.logDebug("Loaded RuneLite class: " + className.replace('/', '.'));
            }

            // When RuneLite main class loads, connect the bot API
            if ("net/runelite/client/RuneLite".equals(className)) {
                DebugManager.logInfo("RuneLite main class loaded! Injection successful.");
                // Initialize API (eager singleton instantiation ensures module setup/logging)
                ApiManager.get();
            }

            // Return null to keep class as-is unless you want to modify the bytecode.
            return null;
        }
    }
}