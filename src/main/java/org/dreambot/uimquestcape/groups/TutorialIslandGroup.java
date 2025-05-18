package org.dreambot.uimquestcape.groups;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.State;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.states.tutorial.*;
import org.dreambot.uimquestcape.util.QuestVarbitManager;
import org.dreambot.uimquestcape.util.StateGroup;

/**
 * Group for all Tutorial Island states
 */
public class TutorialIslandGroup extends StateGroup {
    // Varbit constants for Tutorial Island progress
    private static final int TUTORIAL_PROGRESS_VARBIT = 281;
    
    // Progress values for different stages
    private static final int GIELINOR_GUIDE_STAGE = 0;
    private static final int SURVIVAL_EXPERT_STAGE = 10;
    private static final int MASTER_CHEF_STAGE = 20;
    private static final int QUEST_GUIDE_STAGE = 30;
    private static final int MINING_INSTRUCTOR_STAGE = 40;
    private static final int COMBAT_INSTRUCTOR_STAGE = 50;
    private static final int FINANCIAL_ADVISOR_STAGE = 60;
    private static final int PRAYER_INSTRUCTOR_STAGE = 70;
    private static final int MAGIC_INSTRUCTOR_STAGE = 80;
    private static final int TUTORIAL_COMPLETE = 1000;
    
    public TutorialIslandGroup(UIMQuestCape script) {
        super(script, "TutorialIsland");
        registerStates();
    }
    
    private void registerStates() {
        addState(new CreateAccountState(getScript()));
        addState(new GielinorGuideState(getScript()));
        addState(new SurvivalExpertState(getScript()));
        addState(new MasterChefState(getScript()));
        addState(new QuestGuideState(getScript()));
        addState(new MiningInstructorState(getScript()));
        addState(new CombatInstructorState(getScript()));
        addState(new FinancialAdvisorState(getScript()));
        addState(new PrayerInstructorState(getScript()));
        addState(new MagicInstructorState(getScript()));
        addState(new TutorialCompletedState(getScript()));
        
        // Link states in sequence
        linkStates();
    }
    
    private void linkStates() {
        // Set up the sequence of states
        getStateByName("CreateAccountState").setNextState(getStateByName("GielinorGuideState"));
        getStateByName("GielinorGuideState").setNextState(getStateByName("SurvivalExpertState"));
        getStateByName("SurvivalExpertState").setNextState(getStateByName("MasterChefState"));
        getStateByName("MasterChefState").setNextState(getStateByName("QuestGuideState"));
        getStateByName("QuestGuideState").setNextState(getStateByName("MiningInstructorState"));
        getStateByName("MiningInstructorState").setNextState(getStateByName("CombatInstructorState"));
        getStateByName("CombatInstructorState").setNextState(getStateByName("FinancialAdvisorState"));
        getStateByName("FinancialAdvisorState").setNextState(getStateByName("PrayerInstructorState"));
        getStateByName("PrayerInstructorState").setNextState(getStateByName("MagicInstructorState"));
        getStateByName("MagicInstructorState").setNextState(getStateByName("TutorialCompletedState"));
    }
    
    @Override
    public State getInitialState() {
        return getStateByName("CreateAccountState");
    }
    
    @Override
    public boolean requirementsMet() {
        // Tutorial Island is the starting point, so requirements are always met
        return true;
    }
    
    @Override
    public State determineCurrentState() {
        int progress = QuestVarbitManager.getVarbit(TUTORIAL_PROGRESS_VARBIT);
        
        // Determine current state based on tutorial progress
        if (progress >= TUTORIAL_COMPLETE) {
            markCompleted();
            return null; // Tutorial is completed, move to next group
        } else if (progress >= MAGIC_INSTRUCTOR_STAGE) {
            return getStateByName("MagicInstructorState");
        } else if (progress >= PRAYER_INSTRUCTOR_STAGE) {
            return getStateByName("PrayerInstructorState");
        } else if (progress >= FINANCIAL_ADVISOR_STAGE) {
            return getStateByName("FinancialAdvisorState");
        } else if (progress >= COMBAT_INSTRUCTOR_STAGE) {
            return getStateByName("CombatInstructorState");
        } else if (progress >= MINING_INSTRUCTOR_STAGE) {
            return getStateByName("MiningInstructorState");
        } else if (progress >= QUEST_GUIDE_STAGE) {
            return getStateByName("QuestGuideState");
        } else if (progress >= MASTER_CHEF_STAGE) {
            return getStateByName("MasterChefState");
        } else if (progress >= SURVIVAL_EXPERT_STAGE) {
            return getStateByName("SurvivalExpertState");
        } else if (progress >= GIELINOR_GUIDE_STAGE) {
            return getStateByName("GielinorGuideState");
        } else {
            return getStateByName("CreateAccountState");
        }
    }
    
    // Method to check if player is on Tutorial Island
    public static boolean isOnTutorialIsland() {
        Area tutorialIslandArea = new Area(3054, 3133, 3156, 3056);
        return tutorialIslandArea.contains(Walking.getPlayerLocation()) && 
               QuestVarbitManager.getVarbit(TUTORIAL_PROGRESS_VARBIT) < TUTORIAL_COMPLETE;
    }
}