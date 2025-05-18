/**
 * FILENAME: UIMInventoryManager.java
 * Path: src/main/java/org/dreambot/uimquestcape/util/UIMInventoryManager.java
 */

package org.dreambot.uimquestcape.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * Handles mechanics specific to Ultimate Ironman inventory management
 */
public class UIMInventoryManager {
    private final UIMQuestCape script;
    private final DeathPileManager deathPileManager;
    private final LootingBagManager lootingBagManager;
    private final Map<String, Integer> essentialItems = new HashMap<>();
    
    public UIMInventoryManager(UIMQuestCape script) {
        this.script = script;
        this.deathPileManager = new DeathPileManager(script);
        this.lootingBagManager = new LootingBagManager(script);
        initializeEssentialItems();
    }
    
    /**
     * Initialize map of essential items that should be prioritized
     */
    private void initializeEssentialItems() {
        // Tools and equipment
        essentialItems.put("Hammer", 1);
        essentialItems.put("Knife", 1);
        essentialItems.put("Tinderbox", 1);
        essentialItems.put("Axe", 1); // Any axe
        essentialItems.put("Pickaxe", 1); // Any pickaxe
        essentialItems.put("Spade", 1);
        essentialItems.put("Chisel", 1);
        
        // Combat essentials
        essentialItems.put("Dramen staff", 1);
        essentialItems.put("Anti-dragon shield", 1);
        essentialItems.put("Games necklace", 1);
        essentialItems.put("Amulet of glory", 1);
        
        // Consumables
        essentialItems.put("Law rune", 10);
        essentialItems.put("Air rune", 20);
        essentialItems.put("Fire rune", 20);
        essentialItems.put("Water rune", 10);
        essentialItems.put("Earth rune", 10);
        essentialItems.put("Nature rune", 10);
        
        // UIM specific
        essentialItems.put("Looting bag", 1);
        essentialItems.put("Rune pouch", 1);
    }
    
    /**
     * Check if we have an essential item
     * @param itemName Name of the item
     * @return true if we have the item
     */
    public boolean hasEssentialItem(String itemName) {
        // Special case for looting bag
        if (itemName.equals("Looting bag")) {
            return lootingBagManager.hasLootingBag();
        }
        
        // Check for any axe or pickaxe
        if (itemName.equals("Axe")) {
            return Inventory.contains(item -> 
                item.getName().contains("axe") && !item.getName().equals("Pickaxe")
            );
        }
        
        if (itemName.equals("Pickaxe")) {
            return Inventory.contains(item -> 
                item.getName().contains("pickaxe")
            );
        }
        
        // Regular item check
        return Inventory.contains(itemName);
    }
    
    /**
     * Check if we have enough of an essential item
     * @param itemName Name of the item
     * @return true if we have enough
     */
    public boolean hasEnoughEssentialItem(String itemName) {
        if (!essentialItems.containsKey(itemName)) {
            return true; // Not an essential item
        }
        
        int requiredCount = essentialItems.get(itemName);
        int currentCount = Inventory.count(itemName);
        
        return currentCount >= requiredCount;
    }
    
    /**
     * Free up inventory space using UIM techniques
     * @param slotsNeeded Number of empty slots needed
     * @return true if operation succeeded
     */
    public boolean freeInventorySpace(int slotsNeeded) {
        int currentEmptySlots = Inventory.emptySlotCount();
        
        if (currentEmptySlots >= slotsNeeded) {
            return true; // Already have enough space
        }
        
        // Try using looting bag first
        if (lootingBagManager.hasLootingBag()) {
            // Need to be in wilderness to use looting bag
            if (isInWilderness()) {
                if (lootingBagManager.storeValuableItems()) {
                    Logger.log("Stored items in looting bag");
                    if (Inventory.emptySlotCount() >= slotsNeeded) {
                        return true;
                    }
                }
            } else {
                // Travel to wilderness to use looting bag
                Logger.log("Traveling to wilderness to use looting bag");
                travelToWilderness();
                return false; // Need to wait until we're in wilderness
            }
        }
        
        // If looting bag isn't available or isn't enough, use death pile
        Logger.log("Using death pile to free inventory space");
        return useDeathPile(slotsNeeded);
    }
    
    /**
     * Use a death pile to free inventory space
     * @param slotsNeeded Number of slots needed
     * @return true if death pile created successfully
     */
    public boolean useDeathPile(int slotsNeeded) {
        // If we already have an active death pile
        if (deathPileManager.isDeathPileActive()) {
            Logger.log("Death pile already active with " + 
                      deathPileManager.getDeathPileTimeRemaining() + 
                      " minutes remaining");
            
            // If we need more items from the death pile
            if (Inventory.emptySlotCount() < slotsNeeded) {
                return deathPileManager.collectDeathPile();
            }
            
            return true;
        }
        
        // Create a new death pile
        // Determine which items to keep
        List<String> itemsToKeep = new ArrayList<>();
        
        // Always keep essential items
        for (String essentialItem : essentialItems.keySet()) {
            if (hasEssentialItem(essentialItem)) {
                itemsToKeep.add(essentialItem);
            }
        }
        
        // Add quest-specific items if needed
        // This would use QuestVarbitManager to determine which quest items to keep
        
        // Create the death pile at a safe location
        Tile currentLocation = Players.getLocal().getTile();
        return deathPileManager.createDeathPile(currentLocation, itemsToKeep);
    }
    
    /**
     * Check if player is in the wilderness
     * @return true if in wilderness
     */
    private boolean isInWilderness() {
        // Check wilderness level via widget
        return PlayerSettings.getBitValue(5963) > 0;
    }
    
    /**
     * Travel to a low-level wilderness area
     */
    private void travelToWilderness() {
        // Travel to level 1 wilderness north of Edgeville
        Tile wildyTile = new Tile(3087, 3520, 0);
        Walking.walk(wildyTile);
    }
    
    /**
     * Manage STASH units for UIM storage
     * @param stashId The ID of the STASH unit
     * @param items List of items to store or retrieve
     * @param action "store" or "retrieve"
     * @return true if operation succeeded
     */
    public boolean manageSTASHUnit(int stashId, String[] items, String action) {
        // Find and interact with STASH unit
        // This would be a placeholder implementation
        return false;
    }
    
    /**
     * Prioritize which items to keep when inventory is full
     * @param newItem The new item being obtained
     * @return Name of item to drop, or null if nothing should be dropped
     */
    public String getItemToDropForSpace(String newItem) {
        // If the new item is essential, find a non-essential item to drop
        if (essentialItems.containsKey(newItem)) {
            for (int i = 0; i < 28; i++) {
                if (Inventory.getItemInSlot(i) != null) {
                    String itemName = Inventory.getItemInSlot(i).getName();
                    if (!essentialItems.containsKey(itemName)) {
                        return itemName;
                    }
                }
            }
        }
        
        // If the new item isn't essential, don't drop anything
        return null;
    }
}