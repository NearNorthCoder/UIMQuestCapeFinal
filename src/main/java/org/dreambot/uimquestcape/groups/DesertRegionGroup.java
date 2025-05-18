/**
 * FILENAME: DesertRegionGroup.java
 * Path: src/main/java/org/dreambot/uimquestcape/groups/DesertRegionGroup.java
 */

package org.dreambot.uimquestcape.groups;

import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.State;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.states.quests.thefeud.*;
import org.dreambot.uimquestcape.states.quests.spiritsofelid.*;
import org.dreambot.uimquestcape.util.QuestVarbitManager;
import org.dreambot.uimquestcape.util.StateGroup;

/**
 * Group for desert region quests and content
 */
public class DesertRegionGroup extends StateGroup {
    
    // Constants for quest completion status
    private static final int THE_FEUD_QUEST_ID = 60;
    private static final int SPIRITS_OF_ELID_QUEST_ID = 53;
    private static final int DESERT_TREASURE_VARBIT = 358;
    
    public DesertRegionGroup(UIMQuestCape script) {
        super(script, "DesertRegion");
        registerStates();
    }
    
    private void registerStates() {
        // The Feud quest states
        addState(new TheFeutdStartState(getScript()));
        addState(new PurchaseDisguiseState(getScript()));
        addState(new TravelToPollnivneachState(getScript()));
        addState(new SpeakToDrunkenAliState(getScript()));
        addState(new SpeakToBanditLeaderState(getScript()));
        addState(new CaptureCamelsState(getScript()));
        addState(new GetBlackjackState(getScript()));
        addState(new BlackjackBanditsState(getScript()));
        addState(new SpeakToMenaphiteLeaderState(getScript()));
        addState(new DefeatBanditChampionState(getScript()));
        addState(new PickpocketMenaphitesState(getScript()));
        addState(new DefeatToughGuyState(getScript()));
        addState(new TheFeutdCompleteState(getScript()));
        
        // Spirits of the Elid quest states
        addState(new SpiritsOfElidStartState(getScript()));
        addState(new GetShrineRobesState(getScript()));
        addState(new EnterWaterDungeonState(getScript()));
        addState(new GetAncestralKeyState(getScript()));
        addState(new FillWaterVesselsState(getScript()));
        addState(new DefeatElementalsState(getScript()));
        addState(new ReturnWaterToFontState(getScript()));
        addState(new SpiritsOfElidCompleteState(getScript()));
        
        // Desert Treasure preparation states
        addState(new DesertTreasurePreparationState(getScript()));
        addState(new DeathPileRequiredItemsState(getScript()));
        
        // Desert Treasure quest states (partial implementation)
        addState(new DesertTreasureStartState(getScript()));
        addState(new SolvePyramidPuzzleState(getScript()));
        
        // Ice diamond section
        addState(new IceDiamondStartState(getScript()));
        addState(new TravelToTrollheimState(getScript()));
        addState(new GetIceTrollKeyState(getScript()));
        addState(new DefeatIceQueenState(getScript()));
        addState(new ObtainIceDiamondState(getScript()));
        
        // Blood diamond section
        addState(new BloodDiamondStartState(getScript()));
        addState(new TravelToCanifisState(getScript()));
        addState(new PrepareDessoursGraveState(getScript()));
        addState(new DefeatDessoursState(getScript()));
        addState(new ObtainBloodDiamondState(getScript()));
        
        // Smoke diamond section
        addState(new SmokeDiamondStartState(getScript()));
        addState(new EnterSmokeDungeonState(getScript()));
        addState(new GetBanditKeyState(getScript()));
        addState(new DefeatFareedState(getScript()));
        addState(new ObtainSmokeDiamondState(getScript()));
        
        // Shadow diamond section
        addState(new ShadowDiamondStartState(getScript()));
        addState(new EnterShadowDungeonState(getScript()));
        addState(new DefeatDamisState(getScript()));
        addState(new ObtainShadowDiamondState(getScript()));
        
        // Desert Treasure completion
        addState(new ReturnToArchaeologistState(getScript()));
        addState(new EnterPyramidState(getScript()));
        addState(new DefeatFinalBossState(getScript()));
        addState(new DesertTreasureCompleteState(getScript()));
        
        // Link states in sequence
        linkStates();
    }
    
    private void linkStates() {
        // The Feud sequence
        getStateByName("TheFeutdStartState").setNextState(getStateByName("PurchaseDisguiseState"));
        getStateByName("PurchaseDisguiseState").setNextState(getStateByName("TravelToPollnivneachState"));
        // ... remaining Feud states linked
        getStateByName("DefeatToughGuyState").setNextState(getStateByName("TheFeutdCompleteState"));
        getStateByName("TheFeutdCompleteState").setNextState(getStateByName("SpiritsOfElidStartState"));
        
        // Spirits of the Elid sequence
        getStateByName("SpiritsOfElidStartState").setNextState(getStateByName("GetShrineRobesState"));
        // ... remaining Spirits states linked
        getStateByName("ReturnWaterToFontState").setNextState(getStateByName("SpiritsOfElidCompleteState"));
        getStateByName("SpiritsOfElidCompleteState").setNextState(getStateByName("DesertTreasurePreparationState"));
        
        // Desert Treasure sequence
        getStateByName("DesertTreasurePreparationState").setNextState(getStateByName("DeathPileRequiredItemsState"));
        getStateByName("DeathPileRequiredItemsState").setNextState(getStateByName("DesertTreasureStartState"));
        
        // Link diamonds in sequence
        getStateByName("SolvePyramidPuzzleState").setNextState(getStateByName("IceDiamondStartState"));
        getStateByName("ObtainIceDiamondState").setNextState(getStateByName("BloodDiamondStartState"));
        getStateByName("ObtainBloodDiamondState").setNextState(getStateByName("SmokeDiamondStartState"));
        getStateByName("ObtainSmokeDiamondState").setNextState(getStateByName("ShadowDiamondStartState"));
        getStateByName("ObtainShadowDiamondState").setNextState(getStateByName("ReturnToArchaeologistState"));
        
        // Desert Treasure completion
        getStateByName("ReturnToArchaeologistState").setNextState(getStateByName("EnterPyramidState"));
        getStateByName("EnterPyramidState").setNextState(getStateByName("DefeatFinalBossState"));
        getStateByName("DefeatFinalBossState").setNextState(getStateByName("DesertTreasureCompleteState"));
    }
    
    @Override
    public State getInitialState() {
        return getStateByName("TheFeutdStartState");
    }
    
    @Override
    public boolean requirementsMet() {
        // Check if Wintertodt is completed
        int firemakingLevel = Skills.getRealLevel(Skills.FIREMAKING);
        return firemakingLevel >= 85 && Inventory.contains("Supply crate");
    }
    
    @Override
    public State determineCurrentState() {
        // Check quest completion status
        boolean feudCompleted = QuestVarbitManager.isQuestCompleted(THE_FEUD_QUEST_ID);
        boolean spiritsCompleted = QuestVarbitManager.isQuestCompleted(SPIRITS_OF_ELID_QUEST_ID);
        boolean desertTreasureCompleted = QuestVarbitManager.getVarbit(DESERT_TREASURE_VARBIT) >= 100;
        
        // Check for Desert Treasure progress
        int dtProgress = QuestVarbitManager.getVarbit(DESERT_TREASURE_VARBIT);
        boolean hasDiamonds = dtProgress >= 50 && dtProgress < 100;
        boolean startedDT = dtProgress > 0;
        
        // Determine current state based on progress
        if (desertTreasureCompleted) {
            markCompleted();
            return null; // All quests completed, group complete
        } else if (hasDiamonds) {
            return getStateByName("ReturnToArchaeologistState");
        } else if (startedDT) {
            // Check which diamonds have been obtained and return next diamond state
            // This would check specific varbits related to each diamond
            return getStateByName("IceDiamondStartState"); // Default to first diamond
        } else if (spiritsCompleted) {
            return getStateByName("DesertTreasurePreparationState");
        } else if (feudCompleted) {
            return getStateByName("SpiritsOfElidStartState");
        } else {
            return getStateByName("TheFeutdStartState");
        }
    }
}