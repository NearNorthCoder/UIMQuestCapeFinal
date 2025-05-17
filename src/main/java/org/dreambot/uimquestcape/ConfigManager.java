package org.dreambot.uimquestcape;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.dreambot.api.utilities.Logger;

/**
 * Handles saving and loading configuration and progress data
 */
public class ConfigManager {
    private final UIMQuestCape script;
    private final File dataFolder;
    private final File configFile;
    private final File progressFile;
    private Properties config;

    public ConfigManager(UIMQuestCape script) {
        this.script = script;
        this.dataFolder = new File(System.getProperty("user.home") + File.separator + "DreamBot" + File.separator + "UIMQuestCape");
        this.configFile = new File(dataFolder, "config.properties");
        this.progressFile = new File(dataFolder, "progress.dat");
        this.config = new Properties();

        // Create folders if they don't exist
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        // Load existing config if available
        loadConfig();
    }

    private void loadConfig() {
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                config.load(fis);
                Logger.log("Config loaded successfully");
            } catch (IOException e) {
                Logger.error("Error loading config: " + e.getMessage());
                config = new Properties(); // Reset to default
            }
        } else {
            // Create default config
            config.setProperty("resumeOnStart", "true");
            config.setProperty("verboseLogging", "true");
            saveConfig();
        }
    }

    private void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            config.store(fos, "UIM Quest Cape Configuration");
            Logger.log("Config saved successfully");
        } catch (IOException e) {
            Logger.error("Error saving config: " + e.getMessage());
        }
    }

    public void saveProgress(Object data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(progressFile))) {
            oos.writeObject(data);
            Logger.log("Progress saved successfully");
        } catch (IOException e) {
            Logger.error("Error saving progress: " + e.getMessage());
        }
    }

    public Object loadProgress() {
        if (!progressFile.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(progressFile))) {
            Object data = ois.readObject();
            Logger.log("Progress loaded successfully");
            return data;
        } catch (IOException | ClassNotFoundException e) {
            Logger.error("Error loading progress: " + e.getMessage());
            return null;
        }
    }

    public String getConfig(String key) {
        return config.getProperty(key);
    }

    public void setConfig(String key, String value) {
        config.setProperty(key, value);
        saveConfig();
    }

    public boolean getBooleanConfig(String key) {
        return Boolean.parseBoolean(config.getProperty(key, "false"));
    }

    public int getIntConfig(String key) {
        try {
            return Integer.parseInt(config.getProperty(key, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}