package org.dreambot.uimquestcape.states.quests.treegnomevillage;

import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.quest.Quests;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.QuestVarbitManager;

/**
 * State for completing the Tree Gnome Village quest
 */
public class TreeGnomeVillageCompleteState extends AbstractState {
    
    private static final int TREE_GNOME_VILLAGE_QUEST_ID = 62;
    
    public TreeGnomeVillageCompleteState(UIMQuestCape script) {
        super(script, "TreeGnomeVillageCompleteState");
    }
    
    @Override
    public int execute() {
        // Check if quest is already completed
        if (QuestVarbitManager.isQuestCompleted(TREE_GNOME_VILLAGE_QUEST_ID)) {
            Logger.log("Tree Gnome Village quest already completed");
            complete();
            return 600;
        }
        
        // TODO: Implement completion logic
        Logger.log("Returning to King Bolren to complete Tree Gnome Village quest");
        
        // Placeholder implementation - would:
        // 1. Navigate to Tree Gnome Village
        // 2. Talk to King Bolren
        // 3. Complete dialogue
        
        // For now, simulate quest completion
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return !QuestVarbitManager.isQuestCompleted(TREE_GNOME_VILLAGE_QUEST_ID);
    }
}