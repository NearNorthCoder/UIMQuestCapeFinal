package org.dreambot.uimquestcape.states.quests.witchshouse;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.quest.Quests;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.QuestVarbitManager;

/**
 * State for completing the Witch's House quest
 */
public class WitchsHouseCompleteState extends AbstractState {
    
    private static final int WITCHS_HOUSE_QUEST_ID = 20;
    
    public WitchsHouseCompleteState(UIMQuestCape script) {
        super(script, "WitchsHouseCompleteState");
    }
    
    @Override
    public int execute() {
        // Check if quest is already completed
        if (QuestVarbitManager.isQuestCompleted(WITCHS_HOUSE_QUEST_ID)) {
            Logger.log("Witch's House quest already completed");
            complete();
            return 600;
        }
        
        // Check if we have the ball
        if (!Inventory.contains("Ball")) {
            Logger.log("Need to get the ball first");
            return 1000;
        }
        
        // TODO: Implement logic to complete quest
        Logger.log("Returning ball to boy to complete Witch's House");
        
        // Placeholder implementation - would:
        // 1. Exit garden/house
        // 2. Find boy
        // 3. Give ball to boy
        
        // For now, simulate quest completion
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return Inventory.contains("Ball") && 
               !QuestVarbitManager.isQuestCompleted(WITCHS_HOUSE_QUEST_ID);
    }
}