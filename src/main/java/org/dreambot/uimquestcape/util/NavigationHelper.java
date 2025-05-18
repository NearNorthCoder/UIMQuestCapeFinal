package org.dreambot.uimquestcape.util;

import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.walking.path.impl.LocalPath;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * Handles navigation and path finding for the bot
 */
public class NavigationHelper {
    private final UIMQuestCape script;
    private static final int MAX_WALKING_WAIT_TIME = 15000; // 15 seconds
    
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
        
        // Check if we can reach the tile directly
        if (Map.canReach(tile)) {
            // Walk to destination
            Logger.log("Walking to tile: " + tile.getX() + ", " + tile.getY());
            return Walking.walk(tile);
        } else {
            // Try web walking for longer distances
            Logger.log("Using web walking to reach tile: " + tile.getX() + ", " + tile.getY());
            return Walking.walkExact(tile);
        }
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
        Tile destination = area.getRandomTile();
        
        // Check if we can reach the tile directly
        if (Map.canReach(destination)) {
            return Walking.walk(destination);
        } else {
            // Try web walking for longer distances
            return Walking.walkExact(destination);
        }
    }
    
    /**
     * Walks to a specific tile and waits until reached
     * @param tile The destination tile
     * @param timeout Maximum wait time in ms
     * @return true if reached destination
     */
    public boolean walkToAndWait(Tile tile, int timeout) {
        if (Players.getLocal().getTile().distance(tile) < 3) {
            return true; // Already at destination
        }
        
        if (walkTo(tile)) {
            Logger.log("Walking to tile and waiting: " + tile.getX() + ", " + tile.getY());
            
            // Wait for arrival with timeout
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < timeout) {
                if (Players.getLocal().getTile().distance(tile) < 3) {
                    return true; // Reached destination
                }
                
                if (Players.getLocal().isMoving()) {
                    // Reset timeout if we're moving
                    startTime = System.currentTimeMillis();
                }
                
                // If stuck, try again
                if (!Players.getLocal().isMoving() && 
                    System.currentTimeMillis() - startTime > 3000) {
                    walkTo(tile);
                }
                
                Sleep.sleep(300, 600);
            }
            
            return Players.getLocal().getTile().distance(tile) < 3;
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
        return Map.canReach(tile);
    }
    
    /**
     * Navigate through obstacles such as doors, gates, etc.
     * @param obstacle The name of the obstacle
     * @param action The action to perform on the obstacle
     * @return true if successfully navigated
     */
    public boolean navigateObstacle(String obstacle, String action) {
        GameObject gameObject = GameObjects.closest(obstacle);
        
        if (gameObject == null) {
            Logger.error("Could not find obstacle: " + obstacle);
            return false;
        }
        
        if (gameObject.distance() > 5) {
            walkTo(gameObject.getTile());
            Sleep.sleepUntil(() -> gameObject.distance() <= 5, 5000);
        }
        
        if (gameObject.hasAction(action)) {
            Logger.log("Interacting with " + obstacle + " using action: " + action);
            
            boolean result = gameObject.interact(action);
            Sleep.sleep(600, 1200);
            
            // Doors take an extra moment to open
            if (action.contains("Open")) {
                Sleep.sleep(1200, 1800);
            }
            
            return result;
        } else {
            Logger.error("Obstacle does not have action: " + action);
            return false;
        }
    }
    
    /**
     * Navigate along a path of tiles
     * @param path Array of tiles forming a path
     * @return true if successfully navigated to the end of the path
     */
    public boolean followPath(Tile[] path) {
        if (path == null || path.length == 0) {
            return false;
        }
        
        // Create a local path
        LocalPath localPath = new LocalPath(path);
        
        // Follow the path
        Logger.log("Following path of " + path.length + " tiles");
        return localPath.traverse() && Sleep.sleepUntil(
            () -> Players.getLocal().getTile().distance(path[path.length - 1]) < 3, 
            MAX_WALKING_WAIT_TIME
        );
    }
}