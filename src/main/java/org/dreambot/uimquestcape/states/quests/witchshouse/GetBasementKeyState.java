package org.dreambot.uimquestcape.states.quests.witchshouse;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for getting the basement key in Witch's House quest
 */
public class GetBasementKeyState extends AbstractState {
    
    public GetBasementKeyState(UIMQuestCape script) {
        super(script, "GetBasementKeyState");
    }
    
    @Override
    public int execute() {
        // Check if we already have basement key
        if (Inventory.contains("Basement key")) {
            Logger.log("Already have basement key");
            complete();
            return 600;
        }
        
        // TODO: Implement logic to get basement key
        Logger.log("Using magnet on east wall to get basement key");
        
        // Placeholder implementation
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return Inventory.contains("Magnet") && !Inventory.contains("Basement key");
    }
}