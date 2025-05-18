package org.dreambot.uimquestcape.states.earlyessentials;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
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
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.NavigationHelper;

public class AcquireLootingBagState extends AbstractState {
    
    private static final Area EDGEVILLE_AREA = new Area(3081, 3502, 3098, 3487);
    private static final Area EDGEVILLE_DUNGEON_ENTRANCE_AREA = new Area(3092, 3471, 3098, 3466);
    private static final Area EDGEVILLE_DUNGEON_WILDERNESS = new Area(3126, 9917, 3139, 9930);
    
    private final NavigationHelper navigation;
    private boolean inWilderness = false;
    private int killCount = 0;
    private long lastAttackTime = 0;
    
    public AcquireLootingBagState(UIMQuestCape script) {
        super(script, "AcquireLootingBagState");
        this.navigation = new NavigationHelper(script);
    }
    
    @Override
    public int execute() {
        // Check if we already got the looting bag
        if (hasLootingBag()) {
            Logger.log("Looting bag acquired!");
            complete();
            return 600;
        }
        
        // Check if we're in wilderness part of Edgeville dungeon
        if (EDGEVILLE_DUNGEON_WILDERNESS.contains(Players.getLocal())) {
            inWilderness = true;
            return handleWilderness();
        }
        
        // Check if we're in Edgeville
        if (!EDGEVILLE_AREA.contains(Players.getLocal())) {
            Logger.log("Traveling to Edgeville");
            Walking.walkExact(EDGEVILLE_AREA.getCenter());
            return 1000;
        }
        
        // If we're in Edgeville, head to dungeon entrance
        if (!EDGEVILLE_DUNGEON_ENTRANCE_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to Edgeville dungeon entrance");
            Walking.walk(EDGEVILLE_DUNGEON_ENTRANCE_AREA.getCenter());
            return 600;
        }
        
        // Enter the dungeon via trapdoor
        if (enterDungeon()) {
            Logger.log("Entering Edgeville dungeon");
            return 1000;
        }
        
        return 600;
    }
    
    private int handleWilderness() {
        // Check ground for looting bag first
        GroundItem lootingBag = GroundItems.closest("Looting bag");
        if (lootingBag != null) {
            Logger.log("Found looting bag on ground, picking up");
            lootingBag.interact("Take");
            Sleep.sleepUntil(() -> hasLootingBag(), 3000);
            return 600;
        }
        
        // Check if our health is low
        if (Players.getLocal().getHealthPercent() < 50) {
            return handleLowHealth();
        }
        
        // Find a monster to kill
        NPC target = findTarget();
        if (target != null) {
            // If not in combat, attack the target
            if (!Players.getLocal().isInCombat() && 
                System.currentTimeMillis() - lastAttackTime > 5000) {
                Logger.log("Attacking " + target.getName() + " for looting bag");
                target.interact("Attack");
                lastAttackTime = System.currentTimeMillis();
                Sleep.sleepUntil(() -> Players.getLocal().isInCombat(), 3000);
            }
            
            // Wait for combat to finish
            if (Players.getLocal().isInCombat()) {
                // Check if target died
                Sleep.sleepUntil(() -> !target.exists() || !Players.getLocal().isInCombat(), 10000);
                
                if (!target.exists()) {
                    killCount++;
                    Logger.log("Killed " + target.getName() + " (" + killCount + " kills)");
                    
                    // Sleep a bit to let ground items appear
                    Sleep.sleep(600, 1200);
                    
                    // Check for looting bag
                    GroundItem bagDrop = GroundItems.closest("Looting bag");
                    if (bagDrop != null) {
                        bagDrop.interact("Take");
                        Sleep.sleepUntil(() -> hasLootingBag(), 3000);
                    }
                }
            }
            
            return 600;
        } else {
            // If no targets nearby, walk around the wilderness area
            Tile randomTile = EDGEVILLE_DUNGEON_WILDERNESS.getRandomTile();
            Walking.walk(randomTile);
            return 1000;
        }
    }
    
    private boolean enterDungeon() {
        return navigation.navigateObstacle("Trapdoor", "Climb-down");
    }
    
    private NPC findTarget() {
        return NPCs.closest(npc -> 
            npc != null && 
            (npc.getName().equals("Thug") || 
             npc.getName().equals("Hobgoblin") || 
             npc.getName().equals("Bandit")) && 
            npc.hasAction("Attack") && 
            !npc.isInCombat() && 
            EDGEVILLE_DUNGEON_WILDERNESS.contains(npc)
        );
    }
    
    private int handleLowHealth() {
        Logger.log("Health low, looking for food");
        
        // Look for food in inventory
        Item food = Inventory.get(item -> 
            item != null && 
            (item.hasAction("Eat") || 
             item.getName().contains("shark") || 
             item.getName().contains("lobster") || 
             item.getName().contains("tuna") || 
             item.getName().contains("bread"))
        );
        
        if (food != null) {
            Logger.log("Eating " + food.getName());
            food.interact("Eat");
            Sleep.sleep(600, 1200);
        } else {
            // If no food, teleport out or walk to entrance
            Logger.log("No food, leaving wilderness");
            // Walk to dungeon entrance if close enough
            Tile exitTile = new Tile(3131, 9917, 0);
            Walking.walk(exitTile);
            
            // If really low health, teleport
            if (Players.getLocal().getHealthPercent() < 20) {
                // Use teleport item or spell
                Item teleport = Inventory.get(item -> 
                    item != null && 
                    (item.getName().contains("teleport") || 
                     item.getName().contains("games necklace"))
                );
                
                if (teleport != null) {
                    teleport.interact();
                    Sleep.sleep(1200, 2000);
                    return 1000;
                }
            }
        }
        
        return 600;
    }
    
    private boolean hasLootingBag() {
        return Inventory.contains("Looting bag") || 
               Inventory.contains("Looting bag (open)");
    }
    
    @Override
    public boolean canExecute() {
        return !hasLootingBag();
    }
}