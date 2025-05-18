package org.dreambot.uimquestcape.groups;

import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.utilities.Logger;
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
    private static final int WITCHS_HOUSE_QUEST_ID = 20;
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
        addState(new TrainAttackState(getScript()));
        addState(new TrainStrengthState(getScript()));
        
        // Witch's House quest states
        addState(new WitchsHouseStartState(getScript()));
        addState(new GetCheesState(getScript()));
        addState(new MouseHoleState(getScript()));
        addState(new FindMagnetState(getScript()));
        addState(new GetBasementKeyState(getScript()));
        addState(new EnterGardenState(getScript()));
        addState(new DefeatMouseFormsState(getScript()));
        addState(new WitchsHouseCompleteState(getScript()));
        
        // Waterfall Quest states
        addState(new WaterfallStartState(getScript()));
        addState(new SpeakHudonState(getScript()));
        addState(new FindCrateState(getScript()));
        addState(new EnterWaterfallDungeonState(getScript()));
        addState(new GetGlarialsPebbleState(getScript()));
        addState(new EnterGlarialsTombState(getScript()));
        addState(new GetGlarialsAmuletState(getScript()));
        addState(new GetGlarialsUrnState(getScript()));
        addState(new ReturnToWaterfallState(getScript()));
        addState(new NavigateDungeonState(getScript()));
        addState(new UseAmuletOnDoorState(getScript()));
        addState(new UseUrnOnStandState(getScript()));
        addState(new WaterfallCompleteState(getScript()));
        
        // Fight Arena quest states
        addState(new FightArenaStartState(getScript()));
        addState(new KillBouncerState(getScript()));
        addState(new FreePrisonersState(getScript()));
        addState(new DefeatBouncerInArenaState(getScript()));
        addState(new FightArenaCompleteState(getScript()));
        
        // Tree Gnome Village quest states
        addState(new TreeGnomeVillageStartState(getScript()));
        addState(new NavigateMazeState(getScript()));
        addState(new SpeakKingBolrenState(getScript()));
        addState(new GetOrbsState(getScript()));
        addState(new ReturnOrbsState(getScript()));
        addState(new DefeatWarlordState(getScript()));
        addState(new TreeGnomeVillageCompleteState(getScript()));
        
        // Link states in sequence
        linkStates();
    }
    
    private void linkStates() {
        // Too many state transitions to show individually, but they follow the quest guides
        // Example of linking combat training states
        getStateByName("TravelToBarbarianVillageState").setNextState(getStateByName("TrainAttackState"));
        getStateByName("TrainAttackState").setNextState(getStateByName("TrainStrengthState"));
        getStateByName("TrainStrengthState").setNextState(getStateByName("WitchsHouseStartState"));
        
        // Link last state of each quest to first state of next quest
        getStateByName("WitchsHouseCompleteState").setNextState(getStateByName("WaterfallStartState"));
        getStateByName("WaterfallCompleteState").setNextState(getStateByName("FightArenaStartState"));
        getStateByName("FightArenaCompleteState").setNextState(getStateByName("TreeGnomeVillageStartState"));
        
        // Last state would connect to next group's first state
    }
    
    @Override
    public State getInitialState() {
        return getStateByName("TravelToBarbarianVillageState");
    }
    
    @Override
    public boolean requirementsMet() {
        // Check if early game essentials are obtained
        return Inventory.contains("Looting bag") && 
               Inventory.count(995) >= 10000 && 
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
        } else {
            return getStateByName("TrainAttackState");
        }
    }
}