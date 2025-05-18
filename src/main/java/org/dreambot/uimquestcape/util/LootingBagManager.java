package org.dreambot.uimquestcape.util;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.uimquestcape.UIMQuestCape;

import java.util.Arrays;
import java.util.List;

/**
 * Manages looting bag operations for UIM
 */
public class LootingBagManager {
    private final UIMQuestCape script;
    private final NavigationHelper navigation;
    
    // Wilderness areas
    private static final Area EDGEVILLE_WILDERNESS = new Area(3084, 3524, 3097, 3537);
    private static final Area EDGEVILLE_DUNGEON_WILDERNESS = new Area(3126, 9917, 3139, 9930);
    
    // List of valuable items to prioritize for looting bag
    private static final List<String> VALUABLE_ITEMS = Arrays.asList(
        "Coins", "Nature rune", "Death rune", "Blood rune", "Law rune",
        "Dragon scimitar", "Dragon dagger", "Rune platebody", "Rune platelegs",
        "Rune full helm", "Dragonstone", "Diamond", "Ruby", "Sapphire", "Emerald"
    );
    
    public LootingBagManager(UIMQuestCape script) {
        this.script = script;
        this.navigation = new NavigationHelper(script);
    }
    
    /**
     * Checks if player has a looting bag
     * @return true if looting bag is in inventory
     */
    public boolean hasLootingBag() {
        return Inventory.contains("Looting bag") || Inventory.contains("Looting bag (open)");
    }
    
    /**
     * Opens the looting bag
     * @return true if looting bag is opened successfully
     */
    public boolean openLootingBag() {
        if (!hasLootingBag()) {
            Logger.error("No looting bag found in inventory");
            return false;
        }
        
        Item lootingBag = Inventory.get("Looting bag");
        if (lootingBag == null) {
            lootingBag = Inventory.get("Looting bag (open)");
        }
        
        if (lootingBag != null) {
            return lootingBag.interact("Open") && Sleep.sleepUntil(() -> 
                Widgets.getWidgetChild(541, 0) != null && 
                Widgets.getWidgetChild(541, 0).isVisible(), 3000);
        }
        
        return false;
    }
    
    /**
     * Stores an item in the looting bag
     * @param itemName Name of the item to store
     * @return true if item is stored successfully
     */
    public boolean storeInLootingBag(String itemName) {
        // Must be in wilderness to store items
        if (!isInWilderness()) {
            Logger.error("Must be in wilderness to store items in looting bag");
            return false;
        }
        
        if (!hasLootingBag()) {
            Logger.error("No looting bag found in inventory");
            return false;
        }
        
        Item item = Inventory.get(itemName);
        if (item == null) {
            Logger.error("Item not found in inventory: " + itemName);
            return false;
        }
        
        Item lootingBag = Inventory.get("Looting bag");
        if (lootingBag == null) {
            lootingBag = Inventory.get("Looting bag (open)");
        }
        
        if (lootingBag != null) {
            Logger.log("Storing item in looting bag: " + itemName);
            
            // Use item on looting bag
            if (item.useOn(lootingBag)) {
                return Sleep.sleepUntil(() -> !Inventory.contains(itemName), 3000);
            }
        }
        
        return false;
    }
    
    /**
     * Checks if player is in wilderness
     * @return true if in wilderness
     */
    public boolean isInWilderness() {
        // Check wilderness level via the interface
        WidgetChild wildernessLevel = Widgets.getWidgetChild(90, 50);
        if (wildernessLevel != null && wildernessLevel.isVisible()) {
            return true;
        }
        
        // Alternative check using predefined wilderness areas
        Tile playerLocation = Players.getLocal().getTile();
        return EDGEVILLE_WILDERNESS.contains(playerLocation) || 
               EDGEVILLE_DUNGEON_WILDERNESS.contains(playerLocation);
    }
    
    /**
     * Store all valuable items in looting bag
     * @return true if at least one valuable item was stored
     */
    public boolean storeValuableItems() {
        if (!isInWilderness()) {
            Logger.error("Must be in wilderness to store items in looting bag");
            return false;
        }
        
        if (!hasLootingBag()) {
            Logger.error("No looting bag found in inventory");
            return false;
        }
        
        Logger.log("Storing valuable items in looting bag");
        
        boolean storedAny = false;
        
        // First, try to store items from the VALUABLE_ITEMS list
        for (String itemName : VALUABLE_ITEMS) {
            if (Inventory.contains(itemName)) {
                if (storeInLootingBag(itemName)) {
                    storedAny = true;
                }
            }
        }
        
        // If no valuable items were stored, try to store any non-essential items
        if (!storedAny) {
            for (Item item : Inventory.all()) {
                if (item != null && !isEssentialItem(item.getName())) {
                    if (storeInLootingBag(item.getName())) {
                        storedAny = true;
                        break;
                    }
                }
            }
        }
        
        return storedAny;
    }
    
    /**
     * Check if an item is essential and should not be stored
     * @param itemName Name of the item
     * @return true if the item is essential
     */
    private boolean isEssentialItem(String itemName) {
        // Essential items that should not be stored in looting bag
        List<String> essentialItems = Arrays.asList(
            "Looting bag", "Looting bag (open)", "Coins", "Spade", "Hammer", 
            "Knife", "Tinderbox", "Rune pouch", "Dramen staff", "Games necklace"
        );
        
        return essentialItems.contains(itemName) || 
               itemName.contains("teleport") || 
               itemName.contains("food") || 
               itemName.contains("potion") ||
               Equipment.contains(itemName);
    }
    
    /**
     * Acquires a looting bag in Edgeville dungeon
     * @return true if looting bag is acquired
     */
    public boolean acquireLootingBag() {
        if (hasLootingBag()) {
            Logger.log("Already have a looting bag");
            return true;
        }
        
        Logger.log("Attempting to acquire looting bag");
        
        // Navigate to Edgeville dungeon wilderness area
        if (!EDGEVILLE_DUNGEON_WILDERNESS.contains(Players.getLocal())) {
            Logger.log("Traveling to Edgeville dungeon wilderness");
            
            // First get to Edgeville
            Tile edgevilleTile = new Tile(3087, 3496, 0);
            if (Players.getLocal().distance(edgevilleTile) > 50) {
                navigation.webWalk(edgevilleTile);
                Sleep.sleepUntil(() -> Players.getLocal().distance(edgevilleTile) < 5, 10000);
            }
            
            // Enter the dungeon via trapdoor
            if (!navigation.navigateObstacle("Trapdoor", "Climb-down")) {
                return false;
            }
            
            // Walk to wilderness area of dungeon
            navigation.walkToAndWait(EDGEVILLE_DUNGEON_WILDERNESS.getRandomTile(), 10000);
        }
        
        // Kill NPCs until looting bag drops
        int killCount = 0;
        while (!hasLootingBag() && killCount < 50) {
            NPC target = NPCs.closest(npc -> 
                npc != null && 
                (npc.getName().equals("Thug") || npc.getName().equals("Hobgoblin")) && 
                npc.hasAction("Attack") && 
                !npc.isInCombat() && 
                EDGEVILLE_DUNGEON_WILDERNESS.contains(npc)
            );
            
            if (target != null) {
                Logger.log("Attacking " + target.getName() + " for looting bag");
                
                if (target.interact("Attack")) {
                    Sleep.sleepUntil(() -> !target.exists() || Players.getLocal().getHealthPercent() < 50, 30000);
                    killCount++;
                    
                    // Check for looting bag drop
                    Sleep.sleep(600, 1200);
                    if (hasLootingBag()) {
                        Logger.log("Looting bag acquired!");
                        return true;
                    }
                }
            } else {
                Sleep.sleep(600, 1200);
            }
            
            // Eat if health is low
            if (Players.getLocal().getHealthPercent() < 50) {
                Logger.log("Health low, looking for food");
                Item food = Inventory.get(item -> 
                    item != null && 
                    (item.getName().contains("food") || 
                     item.getName().contains("shark") || 
                     item.getName().contains("lobster") || 
                     item.getName().contains("tuna") || 
                     item.getName().contains("bread"))
                );
                
                if (food != null) {
                    food.interact("Eat");
                    Sleep.sleep(600, 1200);
                } else {
                    Logger.error("No food found and health is low");
                    return false;
                }
            }
        }
        
        return hasLootingBag();
    }
}