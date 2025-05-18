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
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for completing Wave 1 as a Collector in Barbarian Assault
 */
public class CompleteCollectorWave1State extends AbstractState {
    
    private static final Area COLLECTOR_ROOM = new Area(
            new Tile(2560, 5130, 0),
            new Tile(2580, 5110, 0)
    );
    
    private boolean waveCompleted = false;
    private String lastCallMessage = "";
    private String currentEggColor = "";
    
    public CompleteCollectorWave1State(UIMQuestCape script) {
        super(script, "CompleteCollectorWave1State");
    }
    
    @Override
    public int execute() {
        // Check if the wave is already completed
        if (waveCompleted) {
            Logger.log("Collector Wave 1 already completed");
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
        
        // Check if in collector room
        if (!COLLECTOR_ROOM.contains(Players.getLocal())) {
            Logger.log("Not in Collector room, finding entrance");
            return findCollectorEntrance();
        }
        
        // Check and update egg color based on Horn calls
        String callMessage = getCallFromInterface();
        if (!callMessage.equals(lastCallMessage)) {
            lastCallMessage = callMessage;
            Logger.log("New call: " + callMessage);
            currentEggColor = getEggColorFromCall(callMessage);
        }
        
        // Collect eggs of the correct color
        if (!currentEggColor.isEmpty()) {
            return collectEggs(currentEggColor);
        }
        
        // Check if wave is completed
        if (checkWaveCleared()) {
            Logger.log("Wave cleared, waiting for completion");
            Sleep.sleepUntil(this::isWaveCompleted, 10000);
            return 1000;
        }
        
        return 600;
    }
    
    private boolean isGameOver() {
        // Check for game over interface
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
    
    private int findCollectorEntrance() {
        // Find and enter collector room
        GameObject collectorEntrance = GameObjects.closest(obj -> 
            obj != null && 
            obj.getName() != null && 
            obj.getName().equals("Collector room") && 
            obj.hasAction("Enter"));
            
        if (collectorEntrance != null) {
            Logger.log("Entering Collector room");
            collectorEntrance.interact("Enter");
            Sleep.sleepUntil(() -> COLLECTOR_ROOM.contains(Players.getLocal()), 5000);
        } else {
            // If entrance not found, walk to approximate location
            Walking.walk(new Tile(2570, 5120, 0));
        }
        
        return 1000;
    }
    
    private String getCallFromInterface() {
        // Get the call from the collector interface
        WidgetChild callWidget = Widgets.getWidgetChild(485, 4);
        if (callWidget != null && callWidget.isVisible()) {
            return callWidget.getText();
        }
        return "";
    }
    
    private String getEggColorFromCall(String callMessage) {
        // Determine which egg color to collect based on the call
        if (callMessage.contains("Red")) {
            return "Red";
        } else if (callMessage.contains("Green")) {
            return "Green";
        } else if (callMessage.contains("Blue")) {
            return "Blue";
        } else if (callMessage.contains("Yellow")) {
            return "Yellow";
        }
        return "";
    }
    
    private int collectEggs(String eggColor) {
        // Get egg dispenser of the correct color
        GameObject eggDispenser = GameObjects.closest(obj -> 
            obj != null && 
            obj.getName() != null && 
            obj.getName().equals(eggColor + " egg dispenser"));
            
        if (eggDispenser != null) {
            // Check if we have space for eggs
            if (Inventory.emptySlotCount() > 0) {
                Logger.log("Taking " + eggColor + " egg");
                eggDispenser.interact("Take");
                Sleep.sleepUntil(() -> Inventory.contains(eggColor.toLowerCase() + " egg"), 3000);
            }
            
            // Load eggs into cannon
            GameObject eggLoader = GameObjects.closest("Egg hopper");
            if (eggLoader != null && Inventory.contains(eggColor.toLowerCase() + " egg")) {
                Logger.log("Loading " + eggColor + " egg into hopper");
                Item egg = Inventory.get(eggColor.toLowerCase() + " egg");
                if (egg != null) {
                    egg.useOn(eggLoader);
                    Sleep.sleep(600, 1200);
                }
            }
        } else {
            Logger.log("Could not find " + eggColor + " egg dispenser");
            // Walk to center of room to locate dispensers
            Walking.walk(new Tile(2570, 5120, 0));
        }
        
        return 600;
    }
    
    private boolean checkWaveCleared() {
        // Check if wave is completed
        int points = getCollectorPoints();
        return points >= 10; // Assuming 10 points needed for completion
    }
    
    private int getCollectorPoints() {
        // Get collector points from interface
        WidgetChild pointsWidget = Widgets.getWidgetChild(485, 7);
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
