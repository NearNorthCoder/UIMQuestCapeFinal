package org.dreambot.uimquestcape.groups;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.State;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.states.lumbridge.*;
import org.dreambot.uimquestcape.util.QuestVarbitManager;
import org.dreambot.uimquestcape.util.StateGroup;

/**
 * Group for initial Lumbridge setup states
 */
public class LumbridgeSetupGroup extends StateGroup {
    
    public LumbridgeSetupGroup(UIMQuestCape script) {
        super(script, "LumbridgeSetup");
        registerStates();
    }
    
    private void registerStates() {
        addState(new SetSpawnPointState(getScript()));
        addState(new CollectItemsState(getScript()));
        addState(new BuyKnifeState(getScript()));
        addState(new BuyHammerState(getScript()));
        addState(new BuyBranchCutterState(getScript()));
        
        // Link states in sequence
        linkStates();
    }
    
    private void linkStates() {
        // Set up the sequence of states
        getStateByName("SetSpawnPointState").setNextState(getStateByName("CollectItemsState"));
        getStateByName("CollectItemsState").setNextState(getStateByName("BuyKnifeState"));
        getStateByName("BuyKnifeState").setNextState(getStateByName("BuyHammerState"));
        getStateByName("BuyHammerState").setNextState(getStateByName("BuyBranchCutterState"));
    }
    
    @Override
    public State getInitialState() {
        return getStateByName("SetSpawnPointState");
    }
    
    @Override
    public boolean requirementsMet() {
        // Check if Tutorial Island is completed
        return TutorialIslandGroup.isOnTutorialIsland() == false && 
               QuestVarbitManager.getVarbit(281) >= 1000;
    }
    
    @Override
    public State determineCurrentState() {
        // Check for possession of key items to determine progress
        boolean hasSetSpawn = hasSpawnSetInLumbridge();
        boolean hasKnife = Inventory.contains("Knife");
        boolean hasHammer = Inventory.contains("Hammer");
        boolean hasBranchCutter = Inventory.contains("Dramen branch cutter");
        
        if (hasBranchCutter) {
            markCompleted();
            return null; // All items acquired, group complete
        } else if (hasHammer) {
            return getStateByName("BuyBranchCutterState");
        } else if (hasKnife) {
            return getStateByName("BuyHammerState");
        } else if (hasSetSpawn) {
            return getStateByName("BuyKnifeState");
        } else {
            return getStateByName("SetSpawnPointState");
        }
    }
    
    // Helper method to check if spawn is set in Lumbridge
    private boolean hasSpawnSetInLumbridge() {
        // In a real implementation, this would check specific game variables
        // For now, we'll use a placeholder logic
        
        // One approach is to check if we've spoken to the Lumbridge Guide
        return NPCs.closest("Lumbridge Guide") != null && 
               NPCs.closest("Lumbridge Guide").distance() < 10 &&
               QuestVarbitManager.getVarbit(281) >= 1000;
    }
}