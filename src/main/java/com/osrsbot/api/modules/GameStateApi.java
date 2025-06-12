package com.osrsbot.api.modules;

import com.osrsbot.debug.DebugManager;

/**
 * GameState API for reading top-level game state information.
 */
public class GameStateApi {
    public enum LoginState { LOGGED_IN, LOGGING_IN, LOGGED_OUT, UNKNOWN }

    public LoginState getLoginState() {
        DebugManager.logApiCall("GameStateApi.getLoginState");
        String state = com.osrsbot.hooks.ClientReflection.getGameState();
        if (state == null) return LoginState.UNKNOWN;
        try {
            if (state.equals("LOGGED_IN")) return LoginState.LOGGED_IN;
            if (state.equals("LOGGING_IN")) return LoginState.LOGGING_IN;
            if (state.equals("LOGGED_OUT")) return LoginState.LOGGED_OUT;
        } catch (Exception e) {
            DebugManager.logException(e);
        }
        return LoginState.UNKNOWN;
    }

    public int getFps() {
        DebugManager.logApiCall("GameStateApi.getFps");
        Integer fps = com.osrsbot.hooks.ClientReflection.getFps();
        return fps != null ? fps : -1;
    }

    public int getTickCount() {
        DebugManager.logApiCall("GameStateApi.getTickCount");
        Integer ticks = com.osrsbot.hooks.ClientReflection.getTickCount();
        return ticks != null ? ticks : -1;
    }
}