package com.osrsbot.api.modules;

import com.osrsbot.debug.DebugManager;

/**
 * GameState API for reading top-level game state information.
 */
public class GameStateApi {
    public enum LoginState { LOGGED_IN, LOGGING_IN, LOGGED_OUT, UNKNOWN }

    public LoginState getLoginState() {
        DebugManager.logApiCall("GameStateApi.getLoginState");
        // TODO: Hook RuneLite client fields/methods for real state
        // Example: Read from client field or use reflection/hooking
        return LoginState.UNKNOWN;
    }

    public int getFps() {
        DebugManager.logApiCall("GameStateApi.getFps");
        // TODO: Retrieve FPS from client
        return -1;
    }

    public int getTickCount() {
        DebugManager.logApiCall("GameStateApi.getTickCount");
        // TODO: Retrieve game tick count from client
        return -1;
    }
}