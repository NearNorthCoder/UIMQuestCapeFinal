package org.dreambot.uimquestcape.groups;

import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.uimquestcape.State;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.states.quests.rfd.*;
import org.dreambot.uimquestcape.states.wintertodt.*;
import org.dreambot.uimquestcape.util.QuestVarbitManager;
import org.dreambot.uimquestcape.util.StateGroup;

/**
 * Group for Recipe for Disaster initial subquests and Wintertodt
 */
public class EarlyMidGameGroup extends StateGroup {

    // Constants for quest completion status
    private static final int RFD_VARBIT = 1850;
    private static final int COOKS_ASSISTANT_STAGE = 1;
    private static final int DWARF_STAGE = 2;

    public EarlyMidGameGroup(UIMQuestCape script) {
        super(script, "EarlyMidGame");
        registerStates();
    }

    private void registerStates() {
        // Recipe for Disaster initial subquests
        addState(new StartRFDState(getScript()));

        // Cook's Assistant subquest
        addState(new GetEggState(getScript()));
        addState(new GetMilkState(getScript()));
        addState(new GetFlourState(getScript()));
        addState(new DeliverCookIngredientsState(getScript()));
        addState(new CooksAssistantCompletedState(getScript()));

        // Dwarf subquest
        addState(new MineClayState(getScript()));
        addState(new MakeDwarvenRockCakeState(getScript()));
        addState(new DeliverCakeState(getScript()));
        addState(new DwarfSubquestCompletedState(getScript()));

        // Wintertodt preparation & completion
        addState(new TrainFiremakingState(getScript()));
        addState(new TravelToZeahState(getScript()));
        addState(new EnterWintertodtState(getScript()));
        addState(new CompleteWintertodtGamesState(getScript()));
        addState(new StoreWintertodtCratesState(getScript()));

        // Link states in sequence
        linkStates();
    }

    private void linkStates() {
        // RFD start
        getStateByName("StartRFDState").setNextState(getStateByName("GetEggState"));

        // Cook's Assistant sequence
        getStateByName("GetEggState").setNextState(getStateByName("GetMilkState"));
        getStateByName("GetMilkState").setNextState(getStateByName("GetFlourState"));
        getStateByName("GetFlourState").setNextState(getStateByName("DeliverCookIngredientsState"));
        getStateByName("DeliverCookIngredientsState").setNextState(getStateByName("CooksAssistantCompletedState"));
        getStateByName("CooksAssistantCompletedState").setNextState(getStateByName("MineClayState"));

        // Dwarf subquest sequence
        getStateByName("MineClayState").setNextState(getStateByName("MakeDwarvenRockCakeState"));
        getStateByName("MakeDwarvenRockCakeState").setNextState(getStateByName("DeliverCakeState"));
        getStateByName("DeliverCakeState").setNextState(getStateByName("DwarfSubquestCompletedState"));
        getStateByName("DwarfSubquestCompletedState").setNextState(getStateByName("TrainFiremakingState"));

        // Wintertodt sequence
        getStateByName("TrainFiremakingState").setNextState(getStateByName("TravelToZeahState"));
        getStateByName("TravelToZeahState").setNextState(getStateByName("EnterWintertodtState"));
        getStateByName("EnterWintertodtState").setNextState(getStateByName("CompleteWintertodtGamesState"));
        getStateByName("CompleteWintertodtGamesState").setNextState(getStateByName("StoreWintertodtCratesState"));
    }

    @Override
    public State getInitialState() {
        return getStateByName("StartRFDState");
    }

    @Override
    public boolean requirementsMet() {
        // Check if fairy rings are unlocked
        return QuestVarbitManager.getVarbit(2328) >= 30; // FAIRY_TALE_2_VARBIT
    }

    @Override
    public State determineCurrentState() {
        // Check RFD subquest progress
        int rfdProgress = QuestVarbitManager.getVarbit(RFD_VARBIT);

        // Check Wintertodt progress
        int firemakingLevel = Skills.getRealLevel(Skills.FIREMAKING);
        boolean completedWintertodt = firemakingLevel >= 85;
        boolean storedCrates = Inventory.contains("Supply crate");

        // Determine current state based on progress
        if (storedCrates && completedWintertodt) {
            markCompleted();
            return null; // Wintertodt completed, group complete
        } else if (completedWintertodt) {
            return getStateByName("StoreWintertodtCratesState");
        } else if (firemakingLevel >= 50) {
            return getStateByName("TravelToZeahState");
        } else if (rfdProgress >= DWARF_STAGE) {
            return getStateByName("TrainFiremakingState");
        } else if (rfdProgress >= COOKS_ASSISTANT_STAGE) {
            return getStateByName("MineClayState");
        } else {
            return getStateByName("StartRFDState");
        }
    }
}