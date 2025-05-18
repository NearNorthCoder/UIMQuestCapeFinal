package org.dreambot.uimquestcape;

import java.util.ArrayList;
import java.util.List;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.groups.*;

@ScriptManifest(
        name = "UIM Quest Cape",
        description = "Completes all quests on an Ultimate Ironman account",
        author = "Your Name",
        version = 1.0,
        category = Category.QUEST
)
public class UIMQuestCape extends AbstractScript {

    // Core state machine components
    private StatManager statManager;
    private StateDetector stateDetector;
    private ErrorHandler errorHandler;
    private ProgressTracker progressTracker;
    private ConfigManager configManager;
    
    // State groups for progression segments
    private List<StateGroup> stateGroups = new ArrayList<>();
    private StateGroup currentGroup;
    
    @Override
    public void onStart() {
        // Initialize components
        statManager = new StatManager(this);
        stateDetector = new StateDetector(this);
        errorHandler = new ErrorHandler(this);
        progressTracker = new ProgressTracker(this);
        configManager = new ConfigManager(this);
        
        // Register all state groups
        registerStateGroups();
        
        // Determine starting group and state based on current progress
        determineStartingPoint();
        
        Logger.log("UIM Quest Cape Bot started with group: " + 
                 (currentGroup != null ? currentGroup.getGroupName() : "None") + 
                 " and state: " + 
                 (statManager.getCurrentState() != null ? statManager.getCurrentState().getName() : "None"));
    }
    
    @Override
    public int onLoop() {
        try {
            // Check for errors or interruptions
            if (errorHandler.isErrorState()) {
                return errorHandler.handleError();
            }
            
            // If current group is complete, move to next group
            if (currentGroup != null && currentGroup.isCompleted()) {
                moveToNextGroup();
            }
            
            // Get current state
            State currentState = statManager.getCurrentState();
            if (currentState == null) {
                Logger.error("No current state - determining new state");
                determineStartingPoint();
                return 1000;
            }
            
            // Log current progress periodically
            progressTracker.updateProgress(currentState);
            
            // Execute current state
            int sleepTime = currentState.execute();
            
            // Check if state is completed
            if (currentState.isCompleted()) {
                State nextState = currentState.getNextState();
                if (nextState != null) {
                    statManager.setCurrentState(nextState);
                    Logger.log("Transitioning to state: " + nextState.getName());
                } else if (currentGroup != null) {
                    // If no next state but group not marked complete, try to determine next state
                    currentGroup.markCompleted();
                    moveToNextGroup();
                }
            }
            
            return sleepTime;
        } catch (Exception e) {
            // Any uncaught exceptions are logged and handled
            errorHandler.registerError(e);
            Logger.error("Error in main loop: " + e.getMessage());
            return errorHandler.getErrorSleepTime();
        }
    }
    
    @Override
    public void onExit() {
        // Save current progress
        progressTracker.saveProgress();
        Logger.log("UIM Quest Cape Bot stopped at state: " + 
                 (statManager.getCurrentState() != null ? statManager.getCurrentState().getName() : "None"));
    }
    
    /**
     * Register all state groups in progression order
     */
    private void registerStateGroups() {
        // Add groups in sequence order
        stateGroups.add(new TutorialIslandGroup(this));
        stateGroups.add(new LumbridgeSetupGroup(this));
        stateGroups.add(new EarlyGameEssentialsGroup(this));
        stateGroups.add(new EarlyCombatQuestsGroup(this));
        stateGroups.add(new ConvenienceUnlocksGroup(this));
        stateGroups.add(new EarlyMidGameGroup(this));
        // Add other groups for Desert region, combat equipment, slayer progression, etc.
        
        // Register all states from all groups with state manager
        for (StateGroup group : stateGroups) {
            for (State state : group.getStates()) {
                statManager.registerState(state);
            }
        }
    }
    
    /**
     * Determine starting point based on current progress
     */
    private void determineStartingPoint() {
        // Find the first non-completed group
        for (StateGroup group : stateGroups) {
            if (!group.isCompleted() && group.requirementsMet()) {
                currentGroup = group;
                State startingState = group.determineCurrentState();
                if (startingState != null) {
                    statManager.setCurrentState(startingState);
                } else {
                    // If no current state in this group, mark as completed and continue
                    group.markCompleted();
                    continue;
                }
                return;
            }
        }
        
        // If all groups are completed or requirements not met for any
        Logger.log("No valid group found - starting from beginning");
        currentGroup = stateGroups.get(0);
        statManager.setCurrentState(currentGroup.getInitialState());
    }
    
    /**
     * Move to the next group in sequence
     */
    private void moveToNextGroup() {
        if (currentGroup == null) {
            determineStartingPoint();
            return;
        }
        
        int currentIndex = stateGroups.indexOf(currentGroup);
        if (currentIndex < stateGroups.size() - 1) {
            StateGroup nextGroup = stateGroups.get(currentIndex + 1);
            if (nextGroup.requirementsMet()) {
                currentGroup = nextGroup;
                State initialState = currentGroup.determineCurrentState();
                if (initialState != null) {
                    statManager.setCurrentState(initialState);
                    Logger.log("Moving to next group: " + currentGroup.getGroupName() + 
                               " with state: " + initialState.getName());
                } else {
                    Logger.error("No valid starting state for group: " + currentGroup.getGroupName());
                    // Mark this group as completed since we can't proceed with it
                    currentGroup.markCompleted();
                    moveToNextGroup(); // Recursively try next group
                }
            } else {
                Logger.error("Requirements not met for next group: " + nextGroup.getGroupName());
                // Reassess current position
                determineStartingPoint();
            }
        } else {
            // All groups completed
            Logger.log("All groups completed! Quest cape achieved!");
            stop();
        }
    }
    
    // Getters for core components
    public StatManager getStateManager() {
        return statManager;
    }
    
    public StateDetector getStateDetector() {
        return stateDetector;
    }
    
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }
    
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}