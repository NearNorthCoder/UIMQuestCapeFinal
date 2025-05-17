package org.dreambot.uimquestcape;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;

@ScriptManifest(
        name = "UIM Quest Cape",
        description = "Completes all quests on an Ultimate Ironman account",
        author = "Your Name",
        version = 1.0,
        category = Category.QUEST
)
public class UIMQuestCape extends AbstractScript {

    // Core state machine components
    private StateManager stateManager;
    private StateDetector stateDetector;
    private ErrorHandler errorHandler;
    private ProgressTracker progressTracker;

    @Override
    public void onStart() {
        // Initialize components
        stateManager = new StateManager(this);
        stateDetector = new StateDetector(this);
        errorHandler = new ErrorHandler(this);
        progressTracker = new ProgressTracker(this);

        // Register all states with state manager
        registerAllStates();

        // Set initial state based on detected progress
        State initialState = stateDetector.determineState();
        stateManager.setCurrentState(initialState);

        Logger.log("UIM Quest Cape Bot started in state: " + initialState.getName());
    }

    @Override
    public int onLoop() {
        try {
            // Check for errors or interruptions
            if (errorHandler.isErrorState()) {
                return errorHandler.handleError();
            }

            // Get current state
            State currentState = stateManager.getCurrentState();

            // Log current progress periodically
            progressTracker.updateProgress(currentState);

            // Execute current state
            int sleepTime = currentState.execute();

            // Check if state is completed
            if (currentState.isCompleted()) {
                State nextState = currentState.getNextState();
                stateManager.setCurrentState(nextState);
                Logger.log("Transitioning to state: " + nextState.getName());
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
        Logger.log("UIM Quest Cape Bot stopped at state: " + stateManager.getCurrentState().getName());
    }

    // Register all states with state manager
    private void registerAllStates() {
        // Tutorial Island States
        stateManager.registerState(new CreateAccountState(this));
        stateManager.registerState(new GielinorGuideState(this));
        // ... many more states registered here

        // Final Quest Cape State
        stateManager.registerState(new ClaimQuestCapeState(this));
    }

    // Getters for core components
    public StateManager getStateManager() {
        return stateManager;
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
}