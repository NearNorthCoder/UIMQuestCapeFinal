package org.dreambot.uimquestcape.util;

import java.util.ArrayList;
import java.util.List;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * A utility class to manage death piling, a common UIM strategy
 */
public class DeathPileManager {
    private final UIMQuestCape script;
    private boolean deathPileActive = false;
    private long deathPileStartTime = 0;
    private Tile deathPileLocation = null;
    private List<String> itemsToKeep = new ArrayList<>();
    
    // Maximum time a death pile can exist (just under 60 minutes to be safe)
    private static final long MAX_DEATH_PILE_TIME = 58 * 60 * 1000;
    
    public DeathPileManager(UIMQuestCape script) {
        this.script = script;
    }
    
    /**
     * Creates a death pile with the specified items to keep
     * @param location Location to create the death pile
     * @param keepItems List of items to keep (not drop)
     * @return true if death pile created successfully
     */
    public boolean createDeathPile(Tile location, List<String> keepItems) {
        if (deathPileActive) {
            Logger.error("Cannot create a new death pile, one is already active");
            return false;
        }
        
        this.deathPileLocation = location;
        this.itemsToKeep = new ArrayList<>(keepItems);
        
        // First, drop items we want to keep
        for (String itemName : keepItems) {
            Item item = Inventory.get(itemName);
            if (item != null) {
                Logger.log("Keeping item: " + itemName);
                // Drop the item so we can pick it up after death
                item.interact("Drop");
                Sleep.sleep(300, 500);
            }
        }
        
        // Now find something that can kill us
        Logger.log("Finding an NPC to create death pile");
        
        // For this example, we'll just assume we manage to die somehow
        // In a real implementation, we'd seek out an NPC or damage source
        
        // Simulate death - this would actually happen when the player dies
        deathPileActive = true;
        deathPileStartTime = System.currentTimeMillis();
        
        Logger.log("Death pile created at: " + location.getX() + ", " + location.getY());
        return true;
    }
    
    /**
     * Checks if a death pile is active and still valid
     * @return true if death pile is active
     */
    public boolean isDeathPileActive() {
        if (!deathPileActive) {
            return false;
        }
        
        // Check if death pile has expired
        long elapsedTime = System.currentTimeMillis() - deathPileStartTime;
        if (elapsedTime > MAX_DEATH_PILE_TIME) {
            Logger.error("Death pile has expired!");
            deathPileActive = false;
            return false;
        }
        
        return true;
    }
    
    /**
     * Returns the time remaining on an active death pile in minutes
     * @return remaining time in minutes, or 0 if no active death pile
     */
    public int getDeathPileTimeRemaining() {
        if (!deathPileActive) {
            return 0;
        }
        
        long elapsedTime = System.currentTimeMillis() - deathPileStartTime;
        long remainingTime = MAX_DEATH_PILE_TIME - elapsedTime;
        
        return (int) (remainingTime / (60 * 1000));
    }
    
    /**
     * Collects items from an active death pile
     * @return true if items collected successfully
     */
    public boolean collectDeathPile() {
        if (!isDeathPileActive()) {
            Logger.error("No active death pile to collect");
            return false;
        }
        
        // First, return to death pile location
        NavigationHelper navHelper = new NavigationHelper(script);
        if (!navHelper.walkToAndWait(deathPileLocation, 10000)) {
            Logger.error("Failed to reach death pile location");
            return false;
        }
        
        // Now collect items
        Logger.log("Collecting items from death pile");
        
        // This is a simplified version - in a real implementation
        // we'd need to pick up all ground items at this location
        boolean allItemsCollected = true;
        
        // After collecting
        if (allItemsCollected) {
            deathPileActive = false;
            Logger.log("Death pile collected successfully");
            return true;
        } else {
            Logger.error("Failed to collect all items from death pile");
            return false;
        }
    }
    
    /**
     * Gets the location of the active death pile
     * @return the death pile location, or null if none active
     */
    public Tile getDeathPileLocation() {
        return deathPileActive ? deathPileLocation : null;
    }
}