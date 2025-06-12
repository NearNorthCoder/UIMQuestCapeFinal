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
                        Thread tickOverlayThread = new Thread(() -> {
                            String lastChat = "";
                            java.util.List<String> lastInventory = java.util.Collections.emptyList();
                            java.util.Map<String, Integer> lastXp = new java.util.HashMap<>();
                            int lastRegionId = -1;
                            java.util.Set<String> lastNpcs = new java.util.HashSet<>();
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
                                            // --- Region change detection ---
                                            java.lang.reflect.Method getLocalPlayer = client.getClass().getMethod("getLocalPlayer");
                                            Object player = getLocalPlayer.invoke(client);
                                            if (player != null) {
                                                java.lang.reflect.Method getWorldLocation = player.getClass().getMethod("getWorldLocation");
                                                Object worldLoc = getWorldLocation.invoke(player);
                                                if (worldLoc != null) {
                                                    java.lang.reflect.Method getRegionId = worldLoc.getClass().getMethod("getRegionID");
                                                    int regionId = ((Number) getRegionId.invoke(worldLoc)).intValue();
                                                    if (regionId != lastRegionId) {
                                                        lastRegionId = regionId;
                                                        com.osrsbot.events.EventBus.publish(
                                                            new com.osrsbot.events.events.RegionChangedEvent(regionId)
                                                        );
                                                    }
                                                }
                                            }

                                            // --- NPC detection ---
                                            java.lang.reflect.Method getNpcs = client.getClass().getMethod("getNpcs");
                                            java.util.List<?> npcs = (java.util.List<?>) getNpcs.invoke(client);
                                            java.util.Set<String> seenNpcs = new java.util.HashSet<>();
                                            for (Object npc : npcs) {
                                                java.lang.reflect.Method getId = npc.getClass().getMethod("getId");
                                                int id = ((Number) getId.invoke(npc)).intValue();
                                                java.lang.reflect.Method getName = npc.getClass().getMethod("getName");
                                                Object nameObj = getName.invoke(npc);
                                                String name = nameObj != null ? nameObj.toString() : ("id_" + id);
                                                java.lang.reflect.Method getWorldLocation = npc.getClass().getMethod("getWorldLocation");
                                                Object npcLoc = getWorldLocation.invoke(npc);
                                                int x = -1, y = -1;
                                                if (npcLoc != null) {
                                                    java.lang.reflect.Method getX = npcLoc.getClass().getMethod("getX");
                                                    java.lang.reflect.Method getY = npcLoc.getClass().getMethod("getY");
                                                    x = ((Number) getX.invoke(npcLoc)).intValue();
                                                    y = ((Number) getY.invoke(npcLoc)).intValue();
                                                }
                                                String key = name + ":" + id + ":" + x + ":" + y;
                                                seenNpcs.add(key);
                                                if (!lastNpcs.contains(key)) {
                                                    com.osrsbot.events.EventBus.publish(
                                                        new com.osrsbot.events.events.NpcDetectedEvent(name, id, x, y)
                                                    );
                                                }
                                            }
                                            lastNpcs = seenNpcs;
                                        }
                                    } catch (Exception ignored) {}

                                    Thread.sleep(600); // OSRS game tick ~600ms
                                } catch (InterruptedException e) {
                                    break;
                                } catch (Exception ex) {
                                    com.osrsbot.debug.DebugManager.logException(ex);
                                    com.osrsbot.debug.DebugManager.surfaceCriticalError("Fatal tick/overlay error: " + ex.getMessage());
                                }
                            }
                        }, "TickAndOverlayLoop");
                        tickOverlayThread.start();

                        // JVM shutdown hook for resource/thread cleanup
                        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                            com.osrsbot.scripts.ScriptManager.stopAll();
                            com.osrsbot.modules.ModuleManager.stopAll();
                            com.osrsbot.debug.DebugManager.shutdown();
                            tickOverlayThread.interrupt();
                            com.osrsbot.debug.DebugManager.logInfo("Shutdown completed.");
                        }, "BotShutdownHook"));

                        // Version check for RuneLite (scaffold)
                        try {
                            Object client = com.osrsbot.hooks.ClientReflection.getClient();
                            if (client != null) {
                                ClassLoader cl = client.getClass().getClassLoader();
                                Class<?> versionClass = cl.loadClass("net.runelite.api.Client");
                                java.lang.Package p = versionClass.getPackage();
                                String version = (p != null) ? p.getImplementationVersion() : null;
                                if (version != null && !version.equals("EXPECTED_VERSION")) {
                                    com.osrsbot.debug.DebugManager.logWarn("RuneLite version mismatch: Detected " + version + ", expected EXPECTED_VERSION");
                                    com.osrsbot.gui.OverlayManager.showInfo("WARNING: RuneLite version mismatch, update hooks!");
                                }
                            }
                        } catch (Exception ignored) {}
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