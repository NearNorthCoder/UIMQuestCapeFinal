package org.dreambot.uimquestcape.states.quests.fightarena;

import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.QuestVarbitManager;

/**
 * State for starting the Fight Arena quest
 */
public class FightArenaStartState extends AbstractState {
    
    private static final Area LADY_SERVIL_AREA = new Area(
            new Tile(2565, 3202, 0),
            new Tile(2575, 3192, 0)
    );
    
    private static final int FIGHT_ARENA_QUEST_ID = 17;
    
    public FightArenaStartState(UIMQuestCape script) {
        super(script, "FightArenaStartState");
    }
    
    @Override
    public int execute() {
        // Check if quest already started
        if (QuestVarbitManager.getQuestStatus(FIGHT_ARENA_QUEST_ID) > 0) {
            Logger.log("Fight Arena quest already started");
            complete();
            return 600;
        }
        
        // TODO: Implement logic to start quest
        Logger.log("Talking to Lady Servil to start Fight Arena quest");
        
        // Placeholder implementation - would:
        // 1. Walk to Lady Servil
        // 2. Talk to her to start quest
        
        // For now, simulate quest start
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return QuestVarbitManager.getQuestStatus(FIGHT_ARENA_QUEST_ID) == 0;
    }
}