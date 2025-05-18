package org.dreambot.uimquestcape.util;

import java.util.ArrayList;
import java.util.List;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * A utility class to manage death piling, a common UIM strategy
 */
public class DeathPileManager {
    private final UIMQuestCape script;
    private final NavigationHelper navigation;
    private boolean deathPileActive = false;
    private long deathPileStartTime = 0;
    private Tile deathPileLocation = null;
    private List<String> itemsToKeep = new ArrayList<>();
    private List<String> itemsInDeathPile = new ArrayList<>();
    
    // Maximum time a death pile can exist (just under 60 minutes to be safe)
    private static final long MAX_DEATH_PILE_TIME = 58 * 60 * 1000;
    
    // Safe areas for death piling
    private static final Area LUMBRIDGE_SAFE_AREA = new Area(3221, 3218, 3225, 3222);
    private static final Area VARROCK_SAFE_AREA = new Area(3211, 3422, 3217, 3429);
    
    public DeathPileManager(UIMQuestCape script) {
        this.script = script;
        this.navigation = new NavigationHelper(script);
    }
    
    /**
     * Creates a death pile with the specified items to keep
     * @param location Location to create the death pile, or null for automatic location
     * @param keepItems List of items to keep (not drop)
     * @return true if death pile created successfully
     */
    public boolean createDeathPile(Tile location, List<String> keepItems) {
        if (deathPileActive) {
            Logger.error("Cannot create a new death pile, one is already active");
            return false;
        }
        
        // If location is null, find a safe spot
        if (location == null) {
            location = findSafeDeathPileLocation();
        }
        
        // Navigate to the death pile location
        if (!navigation.walkToAndWait(location, 10000)) {
            Logger.error("Failed to reach death pile location");
            return false;
        }
        
        this.deathPileLocation = location;
        this.itemsToKeep = new ArrayList<>(keepItems);
        this.itemsInDeathPile = new ArrayList<>();
        
        // First, drop items we want to keep nearby
        for (String itemName : keepItems) {
            Item item = Inventory.get(itemName);
            if (item != null) {
                Logger.log("Setting aside item: " + itemName);
                // Drop the item so we can pick it up after death
                item.interact("Drop");
                Sleep.sleep(300, 500);
            }
        }
        
        // Track what items will be in the death pile
        for (Item item : Inventory.all()) {
            if (item != null) {
                itemsInDeathPile.add(item.getName());
            }
        }
        for (Item item : Equipment.all()) {
            if (item != null) {
                itemsInDeathPile.add(item.getName());
            }
        }
        
        // Find something that can kill us nearby
        Logger.log("Finding an NPC to create death pile");
        
        // First try guards
        NPC guard = NPCs.closest("Guard");
        if (guard != null && guard.distance() < 20) {
            if (attackUntilDeath(guard)) {
                Logger.log("Death pile created successfully at: " + location);
                deathPileActive = true;
                deathPileStartTime = System.currentTimeMillis();
                return true;
            }
        }
        
        // Try rats if no guards
        NPC rat = NPCs.closest("Rat");
        if (rat != null && rat.distance() < 20) {
            if (attackUntilDeath(rat)) {
                Logger.log("Death pile created successfully at: " + location);
                deathPileActive = true;
                deathPileStartTime = System.currentTimeMillis();
                return true;
            }
        }
        
        // No suitable NPCs found
        Logger.error("Could not find an NPC to create death pile");
        return false;
    }
    
    /**
     * Attack an NPC until player dies
     */
    private boolean attackUntilDeath(NPC npc) {
        if (npc == null) return false;
        
        // Lower health as much as possible first
        while (Players.getLocal().getHealthPercent() > 10) {
            // Try to hurt ourselves
            if (Players.getLocal().getHealthPercent() > 20) {
                if (!Players.getLocal().isInCombat()) {
                    npc.interact("Attack");
                    Sleep.sleepUntil(() -> Players.getLocal().isInCombat(), 5000);
                }
            }
            
            Sleep.sleep(600, 1000);
        }
        
        // Let the NPC kill us
        int timeout = 0;
        while (Players.getLocal().exists() && timeout < 30) {
            Sleep.sleep(1000);
            timeout++;
        }
        
        return timeout < 30; // Successfully died within timeout
    }
    
    /**
     * Find a safe location for death piling
     */
    private Tile findSafeDeathPileLocation() {
        // Check which safe area is closest
        double lumbridgeDistance = Players.getLocal().distance(LUMBRIDGE_SAFE_AREA.getCenter());
        double varrockDistance = Players.getLocal().distance(VARROCK_SAFE_AREA.getCenter());
        
        if (lumbridgeDistance <= varrockDistance) {
            return LUMBRIDGE_SAFE_AREA.getRandomTile();
        } else {
            return VARROCK_SAFE_AREA.getRandomTile();
        }
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
        if (!navigation.walkToAndWait(deathPileLocation, 10000)) {
            Logger.error("Failed to reach death pile location");
            return false;
        }
        
        // Now collect items on ground
        Logger.log("Collecting items from death pile");
        
        // First collect kept items that were dropped before death
        for (String itemName : itemsToKeep) {
            GroundItem item = GroundItems.closest(itemName);
            if (item != null && item.distance() < 10) {
                item.interact("Take");
                Sleep.sleepUntil(() -> Inventory.contains(itemName), 3000);
            }
        }
        
        // Collect death pile items
        boolean allItemsCollected = true;
        for (String itemName : itemsInDeathPile) {
            if (Inventory.isFull()) {
                Logger.log("Inventory full, cannot collect all items");
                allItemsCollected = false;
                break;
            }
            
            GroundItem item = GroundItems.closest(itemName);
            if (item != null && item.distance() < 10) {
                item.interact("Take");
                Sleep.sleepUntil(() -> Inventory.contains(itemName), 3000);
            }
        }
        
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