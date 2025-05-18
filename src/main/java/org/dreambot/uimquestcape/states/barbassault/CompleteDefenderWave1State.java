package org.dreambot.uimquestcape.states.barbassault;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for completing Wave 1 as a Defender in Barbarian Assault
 */
public class CompleteDefenderWave1State extends AbstractState {
    
    private static final Area DEFENDER_ROOM = new Area(
            new Tile(2540, 5130, 0),
            new Tile(2560, 5110, 0)
    );
    
    private boolean waveCompleted = false;
    private String lastCallMessage = "";
    private String currentBait = "";
    private long lastBaitCollectTime = 0;
    private int trapsFixed = 0;
    private int runnersTrapped = 0;
    
    public CompleteDefenderWave1State(UIMQuestCape script) {
        super(script, "CompleteDefenderWave1State");
    }
    
    @Override
    public int execute() {
        // Check if the wave is already completed
        if (waveCompleted) {
            Logger.log("Defender Wave 1 already completed");
            complete();
            return 600;
        }
        
        // Handle game over or victory interfaces
        if (isGameOver()) {
            Logger.log("Game over detected, restarting");
            closeGameOverInterface();
            return 1000;
        }
        
        if (isWaveCompleted()) {
            Logger.log("Wave completed!");
            waveCompleted = true;
            complete();
            return 600;
        }
        
        // Check if in defender room
        if (!DEFENDER_ROOM.contains(Players.getLocal())) {
            Logger.log("Not in Defender room, finding entrance");
            return findDefenderEntrance();
        }
        
        // Check and update bait type based on Horn calls
        String callMessage = getCallFromInterface();
        if (!callMessage.equals(lastCallMessage)) {
            lastCallMessage = callMessage;
            Logger.log("New call: " + callMessage);
            currentBait = getBaitTypeFromCall(callMessage);
            lastBaitCollectTime = System.currentTimeMillis();
        }
        
        // Fix trap if needed
        if (isAnyTrapBroken()) {
            fixTrap();
            trapsFixed++;
            return 600;
        }
        
        // Collect bait if needed
        if (needToCollectBait()) {
            collectBait(currentBait);
            lastBaitCollectTime = System.currentTimeMillis();
            return 600;
        }
        
        // Drop bait near trap
        if (hasBait() && !currentBait.isEmpty()) {
            return dropBaitNearTrap(currentBait);
        }
        
        // Check if wave is completed (all runners trapped)
        if (checkWaveCleared()) {
            Logger.log("Wave cleared, waiting for completion");
            Sleep.sleepUntil(this::isWaveCompleted, 10000);
            return 1000;
        }
        
        return 600;
    }
    
    private boolean isGameOver() {
        // Check if the game over interface is open
        WidgetChild gameOverWidget = Widgets.getWidgetChild(312, 0);
        return gameOverWidget != null && gameOverWidget.isVisible();
    }
    
    private void closeGameOverInterface() {
        // Click the close button on game over interface
        WidgetChild closeButton = Widgets.getWidgetChild(312, 19);
        if (closeButton != null && closeButton.isVisible()) {
            closeButton.interact();
            Sleep.sleep(1000, 2000);
        }
    }
    
    private boolean isWaveCompleted() {
        // Check for wave completed interface or messages
        WidgetChild waveCompletedWidget = Widgets.getWidgetChild(311, 0);
        return waveCompletedWidget != null && waveCompletedWidget.isVisible();
    }
    
    private int findDefenderEntrance() {
        // Find and enter defender room
        GameObject defenderEntrance = GameObjects.closest(obj -> 
            obj != null && 
            obj.getName() != null && 
            obj.getName().equals("Defender room") && 
            obj.hasAction("Enter"));
            
        if (defenderEntrance != null) {
            Logger.log("Entering Defender room");
            defenderEntrance.interact("Enter");
            Sleep.sleepUntil(() -> DEFENDER_ROOM.contains(Players.getLocal()), 5000);
        } else {
            // If entrance not found, walk to approximate location
            Walking.walk(new Tile(2550, 5120, 0));
        }
        
        return 1000;
    }
    
    private String getCallFromInterface() {
        // Get the call from the defender interface
        WidgetChild callWidget = Widgets.getWidgetChild(486, 3);
        if (callWidget != null && callWidget.isVisible()) {
            return callWidget.getText();
        }
        return lastCallMessage; // Return last message if can't get new one
    }
    
    private String getBaitTypeFromCall(String callMessage) {
        // Determine the bait type based on the call
        if (callMessage.contains("Red")) {
            return "Red egg";
        } else if (callMessage.contains("Green")) {
            return "Green egg";
        } else if (callMessage.contains("Blue")) {
            return "Blue egg";
        } else if (callMessage.contains("Yellow")) {
            return "Yellow egg";
        } else if (callMessage.contains("Meat")) {
            return "Meat";
        } else if (callMessage.contains("Tofu")) {
            return "Tofu";
        } else if (callMessage.contains("Worms")) {
            return "Worms";
        }
        return ""; // Unknown call
    }
    
    private boolean isAnyTrapBroken() {
        // Check if any trap is broken
        GameObject brokenTrap = GameObjects.closest(obj -> 
            obj != null && 
            obj.getName() != null && 
            obj.getName().equals("Broken trap") && 
            DEFENDER_ROOM.contains(obj));
            
        return brokenTrap != null;
    }
    
    private void fixTrap() {
        // Fix broken trap
        GameObject brokenTrap = GameObjects.closest("Broken trap");
        if (brokenTrap != null) {
            Logger.log("Fixing broken trap");
            brokenTrap.interact("Fix");
            Sleep.sleepUntil(() -> !isAnyTrapBroken(), 5000);
        }
    }
    
    private boolean needToCollectBait() {
        // Check if we need to collect bait
        // Collect bait if we don't have any, or if it's been a while since last collection
        return !hasBait() || System.currentTimeMillis() - lastBaitCollectTime > 30000;
    }
    
    private void collectBait(String baitType) {
        if (baitType.isEmpty()) return;
        
        // Find bait dispenser
        String dispenserName = getBaitDispenserName(baitType);
        GameObject dispenser = GameObjects.closest(dispenserName);
        
        if (dispenser != null) {
            Logger.log("Collecting " + baitType + " from dispenser");
            dispenser.interact("Take");
            Sleep.sleepUntil(() -> hasBait(), 3000);
        } else {
            // Walk to center of room to find dispensers
            Walking.walk(new Tile(2550, 5120, 0));
        }
    }
    
    private String getBaitDispenserName(String baitType) {
        if (baitType.contains("egg")) {
            return "Egg dispenser";
        } else if (baitType.equals("Meat")) {
            return "Meat table";
        } else if (baitType.equals("Tofu")) {
            return "Tofu table";
        } else if (baitType.equals("Worms")) {
            return "Worm hole";
        }
        return "Bait dispenser"; // Default
    }
    
    private boolean hasBait() {
        // Check if player has bait in inventory
        return Inventory.contains("Red egg") || 
               Inventory.contains("Green egg") || 
               Inventory.contains("Blue egg") || 
               Inventory.contains("Yellow egg") || 
               Inventory.contains("Meat") || 
               Inventory.contains("Tofu") || 
               Inventory.contains("Worms");
    }
    
    private int dropBaitNearTrap(String baitType) {
        // Find the trap to drop bait near
        GameObject trap = GameObjects.closest(obj -> 
            obj != null && 
            obj.getName() != null && 
            obj.getName().contains("trap") && 
            !obj.getName().equals("Broken trap") &&
            DEFENDER_ROOM.contains(obj));
            
        if (trap != null) {
            // Get the bait from inventory
            Item bait = null;
            if (baitType.equals("Red egg")) {
                bait = Inventory.get("Red egg");
            } else if (baitType.equals("Green egg")) {
                bait = Inventory.get("Green egg");
            } else if (baitType.equals("Blue egg")) {
                bait = Inventory.get("Blue egg");
            } else if (baitType.equals("Yellow egg")) {
                bait = Inventory.get("Yellow egg");
            } else if (baitType.equals("Meat")) {
                bait = Inventory.get("Meat");
            } else if (baitType.equals("Tofu")) {
                bait = Inventory.get("Tofu");
            } else if (baitType.equals("Worms")) {
                bait = Inventory.get("Worms");
            }
            
            if (bait != null) {
                Logger.log("Dropping " + baitType + " near trap");
                
                // Walk close to trap if needed
                if (trap.distance() > 3) {
                    Walking.walk(trap.getTile());
                    Sleep.sleepUntil(() -> trap.distance() <= 3, 3000);
                }
                
                // Drop the bait
                bait.interact("Drop");
                Sleep.sleep(600, 1200);
                
                // Check if runner was trapped
                NPC runner = NPCs.closest(npc -> 
                    npc != null && 
                    npc.getName() != null && 
                    npc.getName().equals("Penance Runner") && 
                    npc.distance() < 5);
                    
                if (runner == null) {
                    // Runner might have been trapped
                    runnersTrapped++;
                }
            }
        } else {
            // Walk around to find traps
            Walking.walk(new Tile(2550, 5120, 0));
        }
        
        return 600;
    }
    
    private boolean checkWaveCleared() {
        // Check if wave is completed based on number of runners trapped
        return runnersTrapped >= 10 || getDefenderPoints() >= 10;
    }
    
    private int getDefenderPoints() {
        // Get defender points from interface
        WidgetChild pointsWidget = Widgets.getWidgetChild(486, 6);
        if (pointsWidget != null && pointsWidget.isVisible()) {
            try {
                return Integer.parseInt(pointsWidget.getText());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
    
    @Override
    public boolean canExecute() {
        return !waveCompleted;
    }
}