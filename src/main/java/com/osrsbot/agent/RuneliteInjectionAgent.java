package com.osrsbot.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class RuneliteInjectionAgent {
    /**
     * This method is called before the main method of the target JVM (RuneLite) when using -javaagent.
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("[OSRSBot] Agent loaded successfully! Attaching to RuneLite...");

        inst.addTransformer(new RuneliteClassTransformer(), true);
    }

    /**
     * This method is called if the agent is attached to a running JVM.
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("[OSRSBot] Agent attached at runtime!");
        inst.addTransformer(new RuneliteClassTransformer(), true);
    }

    /**
     * Transformer that detects when the RuneLite main class is loaded and prints confirmation.
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

            // RuneLite main class is typically net.runelite.client.RuneLite
            if ("net/runelite/client/RuneLite".equals(className)) {
                System.out.println("[OSRSBot] RuneLite main class loaded! Injection successful.");
                // You can perform bytecode manipulation here for hooks & botting logic.
            }

            // Return null to keep class as-is unless you want to modify the bytecode.
            return null;
        }
    }
}