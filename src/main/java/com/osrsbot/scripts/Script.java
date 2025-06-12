package com.osrsbot.scripts;

import com.osrsbot.modules.BotModule;

/**
 * Base interface for all bot scripts.
 * Scripts are modules with a run() method and can be started/stopped.
 */
public interface Script extends BotModule, Runnable {
    // BotModule: getName(), onStart(), onStop()
    // Runnable: run()
}