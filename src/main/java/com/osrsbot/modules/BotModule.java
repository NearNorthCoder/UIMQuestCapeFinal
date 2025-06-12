package com.osrsbot.modules;

/**
 * Base interface for all pluggable modules/bot plugins.
 */
public interface BotModule {
    String getName();
    void onStart();
    void onStop();
}