package org.dreambot.uimquestcape.groups;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.State;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.states.earlyessentials.*;
import org.dreambot.uimquestcape.util.QuestVarbitManager;
import org.dreambot.uimquestcape.util.StateGroup;

/**
 * Group for early game essentials like looting bag and initial money
 */
public class EarlyGameEssentialsGroup extends StateGroup {
    
    // Constants for early game status
    private static final int STRONGHOLD_VARBIT = 853;
    private static final int STRONGHOLD_COMPLETED = 7;
    
    public EarlyGameEssentialsGroup(UIMQuestCape script) {
        super(script, "EarlyGameEssentials");
        registerStates();
    }
    
    private void registerStates() {
        // Looting bag subgroup
        addState(new TravelToEdgevilleState(getScript()));
        addState(new EnterEdgevilleDungeonState(getScript()));
        addState(new AcquireLootingBagState(getScript()));
        
        // Stronghold of Security subgroup
        addState(new TravelToBarbarianVillageState(getScript()));
        addState(new EnterStrongholdState(getScript()));
        addState(new CompleteFirstFloorState(getScript()));
        addState(new CompleteSecondFloorState(getScript()));
        addState(new CompleteThirdFloorState(getScript()));
        addState(new CompleteFourthFloorState(getScript()));
        
        // Teleport acquisition subgroup
        addState(new TravelToVarrockState(getScript()));
        addState(new BuyFireRunesState(getScript()));
        addState(new BuyAirRunesState(getScript()));
        addState(new BuyLawRunesState(getScript()));
        addState(new TravelToGrandExchangeState(getScript()));
        addState(new BuyGamesNecklaceState(getScript()));
        
        // Link states in sequence
        linkStates();
    }
    
    private void linkStates() {
        // Set up the sequence of states
        
        // Looting bag sequence
        getStateByName("TravelToEdgevilleState").setNextState(getStateByName("EnterEdgevilleDungeonState"));
        getStateByName("EnterEdgevilleDungeonState").setNextState(getStateByName("AcquireLootingBagState"));
        getStateByName("AcquireLootingBagState").setNextState(getStateByName("TravelToBarbarianVillageState"));
        
        // Stronghold sequence
        getStateByName("TravelToBarbarianVillageState").setNextState(getStateByName("EnterStrongholdState"));
        getStateByName("EnterStrongholdState").setNextState(getStateByName("CompleteFirstFloorState"));
        getStateByName("CompleteFirstFloorState").setNextState(getStateByName("CompleteSecondFloorState"));
        getStateByName("CompleteSecondFloorState").setNextState(getStateByName("CompleteThirdFloorState"));
        getStateByName("CompleteThirdFloorState").setNextState(getStateByName("CompleteFourthFloorState"));
        getStateByName("CompleteFourthFloorState").setNextState(getStateByName("TravelToVarrockState"));
        
        // Teleport acquisition sequence
        getStateByName("TravelToVarrockState").setNextState(getStateByName("BuyFireRunesState"));
        getStateByName("BuyFireRunesState").setNextState(getStateByName("BuyAirRunesState"));
        getStateByName("BuyAirRunesState").setNextState(getStateByName("BuyLawRunesState"));
        getStateByName("BuyLawRunesState").setNextState(getStateByName("TravelToGrandExchangeState"));
        getStateByName("TravelToGrandExchangeState").setNextState(getStateByName("BuyGamesNecklaceState"));
    }
    
    @Override
    public State getInitialState() {
        return getStateByName("TravelToEdgevilleState");
    }
    
    @Override
    public boolean requirementsMet() {
        // Check if Lumbridge setup is completed
        return !TutorialIslandGroup.isOnTutorialIsland() && 
               Inventory.contains("Knife") &&
               Inventory.contains("Hammer") &&
               Inventory.contains("Dramen branch cutter");
    }
    
    @Override
    public State determineCurrentState() {
        // Determine progress based on acquired items and completed content
        boolean hasLootingBag = Inventory.contains("Looting bag");
        boolean hasCompletedStronghold = QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) == STRONGHOLD_COMPLETED;
        boolean has10kCoins = Inventory.count(995) >= 10000;
        boolean hasFireRunes = Inventory.contains("Fire rune");
        boolean hasAirRunes = Inventory.contains("Air rune");
        boolean hasLawRunes = Inventory.contains("Law rune");
        boolean hasGamesNecklace = Inventory.contains("Games necklace");
        
        // Determine current state based on progress
        if (hasGamesNecklace) {
            markCompleted();
            return null; // All requirements met, group complete
        } else if (hasLawRunes) {
            return getStateByName("TravelToGrandExchangeState");
        } else if (hasAirRunes) {
            return getStateByName("BuyLawRunesState");
        } else if (hasFireRunes) {
            return getStateByName("BuyAirRunesState");
        } else if (has10kCoins) {
            return getStateByName("TravelToVarrockState");
        } else if (hasCompletedStronghold) {
            return getStateByName("TravelToVarrockState");
        } else if (hasLootingBag) {
            return getStateByName("TravelToBarbarianVillageState");
        } else {
            // No requirements met, start from beginning
            return getStateByName("TravelToEdgevilleState");
        }
    }
}