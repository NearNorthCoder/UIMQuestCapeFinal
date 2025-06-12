package com.osrsbot.api;

import com.osrsbot.api.modules.*;
import com.osrsbot.debug.DebugManager;

/**
 * Singleton entrypoint for all bot API access.
 * Provides sub-APIs for game state, player, inventory, input, world, widgets, chat, etc.
 *
 * Usage: ApiManager.get().player.getName();
 */
public class ApiManager {
    private static final ApiManager INSTANCE = new ApiManager();

    public final GameStateApi gameState;
    public final PlayerApi player;
    public final InventoryApi inventory;
    public final InputApi input;
    public final WorldApi world;
    public final WidgetApi widget;
    public final ChatApi chat;

    private ApiManager() {
        DebugManager.logInfo("ApiManager initialized.");
        this.gameState = new GameStateApi();
        this.player = new PlayerApi();
        this.inventory = new InventoryApi();
        this.input = new InputApi();
        this.world = new WorldApi();
        this.widget = new WidgetApi();
        this.chat = new ChatApi();
    }

    /**
     * Retrieve the singleton instance of ApiManager.
     */
    public static ApiManager get() {
        return INSTANCE;
    }
}