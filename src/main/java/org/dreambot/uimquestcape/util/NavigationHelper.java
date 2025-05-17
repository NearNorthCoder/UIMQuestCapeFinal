package org.dreambot.uimquestcape.util;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * Handles navigation and path finding for the bot
 */
public class NavigationHelper {
    private final UIMQuestCape script;
    
    public NavigationHelper(UIMQuestCape script) {
        this.script = script;
    }
    
    /**
     * Walks to a specific tile
     * @param tile The destination tile
     * @return true if reached destination or walking towards it
     */
    public boolean walkTo(Tile tile) {
        if (tile == null) {
            Logger.error("Cannot walk to null tile");
            return false;
        }
        
        // If we're already at the destination
        if (Players.getLocal().getTile().distance(tile) < 3) {
            return true;
        }
        
        // Walk to destination
        Logger.log("Walking to tile: " + tile.getX() + ", " + tile.getY());
        return Walking.walk(tile);
    }
    
    /**
     * Walks to an area
     * @param area The destination area
     * @return true if reached destination or walking towards it
     */
    public boolean walkTo(Area area) {
        if (area == null) {
            Logger.error("Cannot walk to null area");
            return false;
        }
        
        // If we're already in the area
        if (area.contains(Players.getLocal().getTile())) {
            return true;
        }
        
        // Walk to random tile in area
        Logger.log("Walking to area");
        return Walking.walk(area.getRandomTile());
    }
    
    /**
     * Walks to a specific tile and waits until reached
     * @param tile The destination tile
     * @param timeout Maximum wait time in ms
     * @return true if reached destination
     */
    public boolean walkToAndWait(Tile tile, int timeout) {
        if (walkTo(tile)) {
            Logger.log("Walking to tile and waiting: " + tile.getX() + ", " + tile.getY());
            return Sleep.sleepUntil(() -> Players.getLocal().getTile().distance(tile) < 3, timeout);
        }
        return false;
    }
    
    /**
     * Walks to a destination using web walking (more reliable for long distances)
     * @param destination The destination tile
     * @return true if web walking started successfully
     */
    public boolean webWalk(Tile destination) {
        Logger.log("Web walking to: " + destination.getX() + ", " + destination.getY());
        return Walking.walkExact(destination);
    }
    
    /**
     * Checks if we can reach a tile
     * @param tile The tile to check
     * @return true if the tile is reachable
     */
    public boolean canReach(Tile tile) {
        return Walking.canWalk(tile);
    }
    
    /**
     * Navigate through obstacles such as doors, gates, etc.
     * @param obstacle The name of the obstacle
     * @param action The action to perform on the obstacle
     * @return true if successfully navigated
     */
    public boolean navigateObstacle(String obstacle, String action) {
        // Implementation would find and interact with the obstacle
        Logger.log("Navigating obstacle: " + obstacle + " with action: " + action);
        return false; // Placeholder
    }
}