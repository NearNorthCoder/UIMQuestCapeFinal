package org.dreambot.uimquestcape.states.quests.witchshouse;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for finding the magnet in the Witch's House quest
 */
public class FindMagnetState extends AbstractState {
    
    public FindMagnetState(UIMQuestCape script) {
        super(script, "FindMagnetState");
    }
    
    @Override
    public int execute() {
        // Check if we already have magnet
        if (Inventory.contains("Magnet")) {
            Logger.log("Already have magnet");
            complete();
            return 600;
        }
        
        // TODO: Implement logic to find magnet
        Logger.log("Looking for magnet in drawers");
        
        // Placeholder implementation
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return Inventory.contains("Key") && !Inventory.contains("Magnet");
    }
}

// The remaining states follow the same pattern
