package com.osrsbot.console;

import com.osrsbot.scripts.ScriptManager;
import com.osrsbot.scripts.Script;
import com.osrsbot.modules.ModuleManager;
import com.osrsbot.debug.DebugManager;

import java.util.Scanner;

/**
 * Simple command-line console for live bot control.
 */
public class CommandConsole implements Runnable {
    private volatile boolean running = true;

    @Override
    public void run() {
        DebugManager.logInfo("CommandConsole started. Type 'help' for commands.");
        Scanner scanner = new Scanner(System.in);
        while (running) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line == null) continue;
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0) continue;
            String cmd = parts[0].toLowerCase();

            switch (cmd) {
                case "help" -> printHelp();
                case "list" -> listScripts();
                case "start" -> {
                    if (parts.length > 1) startScript(parts[1]);
                    else System.out.println("Usage: start <script>");
                }
                case "stop" -> {
                    if (parts.length > 1) stopScript(parts[1]);
                    else System.out.println("Usage: stop <script>");
                }
                case "modules" -> listModules();
                case "exit", "quit" -> {
                    running = false;
                    System.out.println("Exiting console...");
                }
                default -> System.out.println("Unknown command. Type 'help'.");
            }
        }
    }

    private void printHelp() {
        System.out.println("""
        Commands:
          help           Show this help menu
          list           List loaded scripts
          start <script> Start a script by name
          stop <script>  Stop a script by name
          modules        List loaded modules
          exit/quit      Exit the console
        """);
    }

    private void listScripts() {
        System.out.println("Loaded scripts:");
        for (Script script : ScriptManager.getScripts()) {
            System.out.println("  " + script.getName());
        }
    }

    private void listModules() {
        System.out.println("Loaded modules:");
        for (var mod : ModuleManager.getModules()) {
            System.out.println("  " + mod.getName());
        }
    }

    private void startScript(String name) {
        for (Script script : ScriptManager.getScripts()) {
            if (script.getName().equalsIgnoreCase(name)) {
                ScriptManager.start(script);
                System.out.println("Started script: " + name);
                return;
            }
        }
        System.out.println("Script not found: " + name);
    }

    private void stopScript(String name) {
        for (Script script : ScriptManager.getScripts()) {
            if (script.getName().equalsIgnoreCase(name)) {
                ScriptManager.stop(script);
                System.out.println("Stopped script: " + name);
                return;
            }
        }
        System.out.println("Script not found: " + name);
    }
}