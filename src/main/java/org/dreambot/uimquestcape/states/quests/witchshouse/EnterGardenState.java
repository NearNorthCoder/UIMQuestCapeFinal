package org.dreambot.uimquestcape.states.quests.witchshouse;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for entering the garden in Witch's House quest
 */
public class EnterGardenState extends AbstractState {
    
    public EnterGardenState(UIMQuestCape script) {
        super(script, "EnterGardenState");
    }
    
    @Override
    public int execute() {
        // TODO: Implement logic to enter garden
        Logger.log("Going through west door into garden");
        
        // Placeholder implementation
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return Inventory.contains("Basement key");
    }
}