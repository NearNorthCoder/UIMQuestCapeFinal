package org.dreambot.uimquestcape.groups;

import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.uimquestcape.State;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.states.earlycombat.*;
import org.dreambot.uimquestcape.states.quests.witchshouse.*;
import org.dreambot.uimquestcape.states.quests.waterfall.*;
import org.dreambot.uimquestcape.states.quests.fightarena.*;
import org.dreambot.uimquestcape.states.quests.treegnomevillage.*;
import org.dreambot.uimquestcape.util.QuestVarbitManager;
import org.dreambot.uimquestcape.util.StateGroup;

/**
 * Group for early combat training and initial quests
 */
public class EarlyCombatQuestsGroup extends StateGroup {
    
    // Constants for quest completion status
    private static final int WITCHS_HOUSE_QUEST_ID = 67;
    private static final int WATERFALL_QUEST_ID = 65;
    private static final int FIGHT_ARENA_QUEST_ID = 17;
    private static final int TREE_GNOME_VILLAGE_QUEST_ID = 62;
    
    public EarlyCombatQuestsGroup(UIMQuestCape script) {
        super(script, "EarlyCombatQuests");
        registerStates();
    }
    
    private void registerStates() {
        // Initial combat training states
        addState(new TravelToBarbarianVillageState(getScript()));
        addState(new GetFoodState(getScript()));
        addState(new WeaponUpgradeState(getScript()));
        addState(new TrainAttackState(getScript()));
        addState(new TrainStrengthState(getScript()));
        
        // Witch's House quest states
        // These would need to be implemented
        addState(new WitchsHouseStartState(getScript()));
        // ... other Witch's House states
        
        // Waterfall Quest states
        // These would need to be implemented
        addState(new WaterfallStartState(getScript()));
        // ... other Waterfall Quest states
        
        // Fight Arena quest states
        // These would need to be implemented
        addState(new FightArenaStartState(getScript()));
        // ... other Fight Arena states
        
        // Tree Gnome Village quest states
        // These would need to be implemented
        addState(new TreeGnomeVillageStartState(getScript()));
        // ... other Tree Gnome Village states
        
        // Link states in sequence
        linkStates();
    }
    
    private void linkStates() {
        // Combat training sequence
        getStateByName("TravelToBarbarianVillageState").setNextState(getStateByName("GetFoodState"));
        getStateByName("GetFoodState").setNextState(getStateByName("WeaponUpgradeState"));
        getStateByName("WeaponUpgradeState").setNextState(getStateByName("TrainAttackState"));
        getStateByName("TrainAttackState").setNextState(getStateByName("TrainStrengthState"));
        getStateByName("TrainStrengthState").setNextState(getStateByName("WitchsHouseStartState"));
        
        // Link last state of each quest to first state of next quest
        // ... these would need to be implemented as the quest states are created
    }
    
    @Override
    public State getInitialState() {
        return getStateByName("TravelToBarbarianVillageState");
    }
    
    @Override
    public boolean requirementsMet() {
        // Check if early game essentials are obtained
        return Inventory.contains("Looting bag") && 
               Inventory.count("Coins") >= 10000 && 
               Inventory.contains("Law rune") &&
               Inventory.contains("Games necklace");
    }
    
    @Override
    public State determineCurrentState() {
        // Check combat stats for training progress
        int attackLevel = Skills.getRealLevel(Skills.ATTACK);
        int strengthLevel = Skills.getRealLevel(Skills.STRENGTH);
        
        // Check quest completion status
        boolean witchsHouseCompleted = QuestVarbitManager.isQuestCompleted(WITCHS_HOUSE_QUEST_ID);
        boolean waterfallCompleted = QuestVarbitManager.isQuestCompleted(WATERFALL_QUEST_ID);
        boolean fightArenaCompleted = QuestVarbitManager.isQuestCompleted(FIGHT_ARENA_QUEST_ID);
        boolean treeGnomeVillageCompleted = QuestVarbitManager.isQuestCompleted(TREE_GNOME_VILLAGE_QUEST_ID);
        
        // Determine current state based on progress
        if (treeGnomeVillageCompleted) {
            markCompleted();
            return null; // All quests completed, group complete
        } else if (fightArenaCompleted) {
            return getStateByName("TreeGnomeVillageStartState");
        } else if (waterfallCompleted) {
            return getStateByName("FightArenaStartState");
        } else if (witchsHouseCompleted) {
            return getStateByName("WaterfallStartState");
        } else if (attackLevel >= 15 && strengthLevel >= 15) {
            return getStateByName("WitchsHouseStartState");
        } else if (attackLevel >= 15) {
            return getStateByName("TrainStrengthState");
        } else if (Inventory.contains("Steel sword") || 
                  Inventory.contains("Mithril sword") || 
                  Inventory.contains("Black sword")) {
            return getStateByName("TrainAttackState");
        } else if (countFood() >= 5) {
            return getStateByName("WeaponUpgradeState");
        } else {
            return getStateByName("GetFoodState");
        }
    }
    
    // Helper method to count food items
    private int countFood() {
        return Inventory.count(item -> 
            item != null && 
            (item.hasAction("Eat") || 
             item.getName().contains("Bread") ||
             item.getName().contains("Cake") ||
             item.getName().contains("Meat") ||
             item.getName().contains("Shrimp") ||
             item.getName().contains("Lobster") ||
             item.getName().contains("Fish"))
        );
    }
}
