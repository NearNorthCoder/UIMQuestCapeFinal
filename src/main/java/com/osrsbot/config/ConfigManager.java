package com.osrsbot.config;

import com.osrsbot.debug.DebugManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple persistent JSON config manager for scripts and modules.
 */
public class ConfigManager {
    private static final String CONFIG_DIR = "./config";
    private static final Map<String, Map<String, Object>> cache = new HashMap<>();

    static {
        File dir = new File(CONFIG_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    public static synchronized Map<String, Object> getConfig(String name) {
        if (cache.containsKey(name)) return cache.get(name);
        File file = new File(CONFIG_DIR, name + ".json");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) json.append(line);
                Map<String, Object> map = parseJson(json.toString());
                cache.put(name, map);
                return map;
            } catch (Exception e) {
                DebugManager.logException(e);
            }
        }
        Map<String, Object> empty = new HashMap<>();
        cache.put(name, empty);
        return empty;
    }

    public static synchronized void saveConfig(String name, Map<String, Object> config) {
        cache.put(name, config);
        File file = new File(CONFIG_DIR, name + ".json");
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            out.print(toJson(config));
        } catch (Exception e) {
            DebugManager.logException(e);
        }
    }

    // Minimal JSON serialization/deserialization
    private static String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (var entry : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":");
            Object value = entry.getValue();
            if (value instanceof Number) sb.append(value.toString());
            else sb.append("\"").append(value.toString()).append("\"");
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    private static Map<String, Object> parseJson(String json) {
        Map<String, Object> map = new HashMap<>();
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                String[] kv = pair.split(":", 2);
                if (kv.length == 2) {
                    String key = kv[0].replaceAll("\"", "").trim();
                    String val = kv[1].trim();
                    if (val.matches("^\".*\"$")) val = val.substring(1, val.length() - 1);
                    map.put(key, val);
                }
            }
        }
        return map;
    }
}