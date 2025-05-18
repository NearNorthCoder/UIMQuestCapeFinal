package org.dreambot.uimquestcape.states.quests.witchshouse;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.QuestVarbitManager;

/**
 * State for starting the Witch's House quest
 */
public class WitchsHouseStartState extends AbstractState {
    
    private static final Area WITCHS_HOUSE_AREA = new Area(
            new Tile(2900, 3473, 0),
            new Tile(2934, 3460, 0)
    );
    
    private static final int WITCHS_HOUSE_QUEST_ID = 20;
    
    public WitchsHouseStartState(UIMQuestCape script) {
        super(script, "WitchsHouseStartState");
    }
    
    @Override
    public int execute() {
        // Check if quest already started
        if (QuestVarbitManager.getQuestStatus(WITCHS_HOUSE_QUEST_ID) > 0) {
            Logger.log("Witch's House quest already started");
            complete();
            return 600;
        }
        
        // TODO: Implement full quest start logic
        Logger.log("Starting Witch's House quest");
        
        // Placeholder implementation - in a real implementation this would:
        // 1. Navigate to the boy outside the house
        // 2. Talk to the boy to start the quest
        
        // Mark as complete so we can move to the next state
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return QuestVarbitManager.getQuestStatus(WITCHS_HOUSE_QUEST_ID) == 0;
    }
}