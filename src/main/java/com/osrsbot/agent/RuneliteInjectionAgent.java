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

                // Attempt to locate and cache the Client instance for hooks
                try {
                    // net.runelite.client.RuneLite holds a static field "client" of type net.runelite.api.Client
                    ClassLoader cl = loader;
                    Class<?> runeLiteClass = cl.loadClass("net.runelite.client.RuneLite");
                    java.lang.reflect.Field clientField = runeLiteClass.getDeclaredField("client");
                    clientField.setAccessible(true);

                    Object clientInstance = clientField.get(null);
                    if (clientInstance != null) {
                        com.osrsbot.hooks.ClientReflection.setClientInstance(clientInstance);
                        DebugManager.logInfo("Cached RuneLite Client instance for API hooks.");

                        // Register modules/plugins
                        com.osrsbot.modules.ModuleManager.register(new com.osrsbot.antiban.AntibanManager());
                        com.osrsbot.modules.ModuleManager.startAll();

                        // Dynamic script loading
                        com.osrsbot.scripts.ScriptManager.loadScriptsFromDirectory();

                        // Register and start example script(s)
                        com.osrsbot.scripts.ScriptManager.register(new com.osrsbot.scripts.examples.AutoChatterScript());
                        com.osrsbot.scripts.ScriptManager.register(new com.osrsbot.scripts.examples.AutoWalkerScript());
                        com.osrsbot.scripts.ScriptManager.register(new com.osrsbot.scripts.examples.ChatLoggerScript());

                        // Dynamically load scripts from "scripts/" directory if present
                        com.osrsbot.scripts.ScriptManager.loadScriptsFromDirectory("scripts");

                        com.osrsbot.scripts.ScriptManager.startAll();

                        // Start the command console in a new thread for live control
                        new Thread(new com.osrsbot.console.CommandConsole(), "CommandConsole").start();

                        // Start game tick polling and overlay updates
                        new Thread(() -> {
                            String lastChat = "";
                            java.util.List<String> lastInventory = java.util.Collections.emptyList();
                            java.util.Map<String, Integer> lastXp = new java.util.HashMap<>();
                            while (true) {
                                try {
                                    com.osrsbot.events.EventBus.publish(
                                        new com.osrsbot.events.events.TickEvent(System.currentTimeMillis())
                                    );
                                    com.osrsbot.gui.OverlayManager.updateOverlay();

                                    // --- Chat message event detection ---
                                    String[] messages = com.osrsbot.api.ApiManager.get().chat.getLatestMessages(1);
                                    if (messages.length > 0 && !messages[0].equals(lastChat)) {
                                        lastChat = messages[0];
                                        com.osrsbot.gui.OverlayManager.setLastChat(lastChat);
                                        com.osrsbot.events.EventBus.publish(
                                            new com.osrsbot.events.events.ChatMessageEvent("unknown", lastChat, "PUBLIC")
                                        );
                                    }

                                    // --- Inventory change event detection ---
                                    var items = com.osrsbot.api.ApiManager.get().inventory.getInventory();
                                    java.util.List<String> names = new java.util.ArrayList<>();
                                    for (var i : items) names.add(i.name);
                                    if (!names.equals(lastInventory)) {
                                        lastInventory = names;
                                        com.osrsbot.gui.OverlayManager.setInventorySnapshot(names);
                                        com.osrsbot.events.EventBus.publish(
                                            new com.osrsbot.events.events.InventoryChangedEvent(names)
                                        );
                                    }

                                    // --- XP change event detection (polls all skills) ---
                                    // Try to get skills and their XP from the client via reflection
                                    try {
                                        Object client = com.osrsbot.hooks.ClientReflection.getClient();
                                        if (client != null) {
                                            java.lang.reflect.Method getSkillExperience = client.getClass().getMethod("getSkillExperience", 
                                                client.getClass().getClassLoader().loadClass("net.runelite.api.Skill"));
                                            Class<?> skillEnum = client.getClass().getClassLoader().loadClass("net.runelite.api.Skill");
                                            for (Object skill : skillEnum.getEnumConstants()) {
                                                String skillName = skill.toString();
                                                int xp = ((Number) getSkillExperience.invoke(client, skill)).intValue();
                                                if (!lastXp.containsKey(skillName) || lastXp.get(skillName) != xp) {
                                                    lastXp.put(skillName, xp);
                                                    com.osrsbot.events.EventBus.publish(
                                                        new com.osrsbot.events.events.XpChangedEvent(skillName, xp)
                                                    );
                                                }
                                            }
                                        }
                                    } catch (Exception ignored) {}

                                    Thread.sleep(600); // OSRS game tick ~600ms
                                } catch (InterruptedException e) {
                                    break;
                                } catch (Exception ex) {
                                    com.osrsbot.debug.DebugManager.logException(ex);
                                }
                            }
                        }, "TickAndOverlayLoop").start();
                    } else {
                        DebugManager.logWarn("Could not find RuneLite Client instance (null).");
                    }
                } catch (Exception e) {
                    DebugManager.logException(e);
                }
            }

            // Return null to keep class as-is unless you want to modify the bytecode.
            return null;
        }
    }
}