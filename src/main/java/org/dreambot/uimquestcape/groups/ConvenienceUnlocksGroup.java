package org.dreambot.uimquestcape.groups;

import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.uimquestcape.State;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.states.magic.*;
import org.dreambot.uimquestcape.states.ardougnediary.*;
import org.dreambot.uimquestcape.states.quests.lostcity.*;
import org.dreambot.uimquestcape.states.quests.fairytale.*;
import org.dreambot.uimquestcape.util.QuestVarbitManager;
import org.dreambot.uimquestcape.util.StateGroup;

/**
 * Group for early convenience unlocks like magic training, cloak, and fairy rings
 */
public class ConvenienceUnlocksGroup extends StateGroup {
    
    // Constants for quest completion status
    private static final int LOST_CITY_QUEST_ID = 31;
    private static final int FAIRY_TALE_1_QUEST_ID = 17;
    private static final int FAIRY_TALE_2_VARBIT = 2328;
    private static final int ARDOUGNE_EASY_DIARY_VARBIT = 4458;
    
    public ConvenienceUnlocksGroup(UIMQuestCape script) {
        super(script, "ConvenienceUnlocks");
        registerStates();
    }
    
    private void registerStates() {
        // Magic training states
        addState(new TravelToVarrockState(getScript()));
        addState(new TrainOnZamorakMagesState(getScript()));
        addState(new HighAlchemyUnlockState(getScript()));
        
        // Ardougne Cloak 1 states
        addState(new TravelToArdougneState(getScript()));
        addState(new SpeakWizardCrompertyState(getScript()));
        addState(new TeleportToEssenceMineState(getScript()));
        addState(new MineEssenceState(getScript()));
        addState(new PrayAtMonasteryState(getScript()));
        addState(new FishAtFishingPlatformState(getScript()));
        addState(new PickpocketStallState(getScript()));
        addState(new FillBucketState(getScript()));
        addState(new SpeakTownCrierState(getScript()));
        addState(new SpeakClocktowerWizardState(getScript()));
        addState(new ClaimArdougneCloak1State(getScript()));
        
        // Lost City quest states
        addState(new LostCityStartState(getScript()));
        addState(new FindDramenTreeState(getScript()));
        addState(new DefeatTreeSpiritState(getScript()));
        addState(new MakeDramenStaffState(getScript()));
        addState(new LostCityCompleteState(getScript()));
        
        // Fairy Tale Part I & II states
        addState(new FairyTale1StartState(getScript()));
        addState(new SpeakFairyGodfatherState(getScript()));
        addState(new SpeakFairyNuffState(getScript()));
        addState(new CollectIngredientsState(getScript()));
        addState(new FairyTale1CompleteState(getScript()));
        addState(new FairyTale2StartState(getScript()));
        addState(new ReadBookState(getScript()));
        addState(new UnlockFairyRingsState(getScript()));
        
        // Link states in sequence
        linkStates();
    }
    
    private void linkStates() {
        // Magic training sequence
        getStateByName("TravelToVarrockState").setNextState(getStateByName("TrainOnZamorakMagesState"));
        getStateByName("TrainOnZamorakMagesState").setNextState(getStateByName("HighAlchemyUnlockState"));
        getStateByName("HighAlchemyUnlockState").setNextState(getStateByName("TravelToArdougneState"));
        
        // Ardougne Cloak sequence (simplified)
        getStateByName("TravelToArdougneState").setNextState(getStateByName("SpeakWizardCrompertyState"));
        // ... other diary tasks linked in sequence
        getStateByName("SpeakClocktowerWizardState").setNextState(getStateByName("ClaimArdougneCloak1State"));
        getStateByName("ClaimArdougneCloak1State").setNextState(getStateByName("LostCityStartState"));
        
        // Lost City quest sequence
        getStateByName("LostCityStartState").setNextState(getStateByName("FindDramenTreeState"));
        getStateByName("FindDramenTreeState").setNextState(getStateByName("DefeatTreeSpiritState"));
        getStateByName("DefeatTreeSpiritState").setNextState(getStateByName("MakeDramenStaffState"));
        getStateByName("MakeDramenStaffState").setNextState(getStateByName("LostCityCompleteState"));
        getStateByName("LostCityCompleteState").setNextState(getStateByName("FairyTale1StartState"));
        
        // Fairy Tale quests sequence
        getStateByName("FairyTale1StartState").setNextState(getStateByName("SpeakFairyGodfatherState"));
        // ... other fairy tale 1 tasks linked in sequence
        getStateByName("FairyTale1CompleteState").setNextState(getStateByName("FairyTale2StartState"));
        getStateByName("FairyTale2StartState").setNextState(getStateByName("ReadBookState"));
        getStateByName("ReadBookState").setNextState(getStateByName("UnlockFairyRingsState"));
    }
    
    @Override
    public State getInitialState() {
        return getStateByName("TravelToVarrockState");
    }
    
    @Override
    public boolean requirementsMet() {
        // Check if previous quests are completed
        return QuestVarbitManager.isQuestCompleted(TREE_GNOME_VILLAGE_QUEST_ID) && 
               QuestVarbitManager.isQuestCompleted(FIGHT_ARENA_QUEST_ID) &&
               QuestVarbitManager.isQuestCompleted(WATERFALL_QUEST_ID) &&
               QuestVarbitManager.isQuestCompleted(WITCHS_HOUSE_QUEST_ID);
    }
    
    @Override
    public State determineCurrentState() {
        // Check magic level for training progress
        int magicLevel = Skills.getRealLevel(Skills.MAGIC);
        
        // Check quest and diary completion status
        boolean hasDramenStaff = Inventory.contains("Dramen staff");
        boolean lostCityCompleted = QuestVarbitManager.isQuestCompleted(LOST_CITY_QUEST_ID);
        boolean fairyTale1Completed = QuestVarbitManager.isQuestCompleted(FAIRY_TALE_1_QUEST_ID);
        boolean fairyRingsUnlocked = QuestVarbitManager.getVarbit(FAIRY_TALE_2_VARBIT) >= 30;
        boolean ardougneEasyCompleted = QuestVarbitManager.getVarbit(ARDOUGNE_EASY_DIARY_VARBIT) >= 100;
        
        // Determine current state based on progress
        if (fairyRingsUnlocked) {
            markCompleted();
            return null; // Fairy rings unlocked, group complete
        } else if (fairyTale1Completed) {
            return getStateByName("FairyTale2StartState");
        } else if (lostCityCompleted) {
            return getStateByName("FairyTale1StartState");
        } else if (hasDramenStaff) {
            return getStateByName("LostCityCompleteState");
        } else if (ardougneEasyCompleted) {
            return getStateByName("LostCityStartState");
        } else if (magicLevel >= 55) {
            return getStateByName("TravelToArdougneState");
        } else {
            return getStateByName("TrainOnZamorakMagesState");
        }
    }
}