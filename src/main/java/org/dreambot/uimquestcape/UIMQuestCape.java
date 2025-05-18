package org.dreambot.uimquestcape;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.groups.EarlyGameEssentialsGroup;
import org.dreambot.uimquestcape.states.IdleState;

/**
 * Main script class for the UIM Quest Cape bot.
 * Implements a state machine design pattern to handle the complex sequence of tasks.
 */
@ScriptManifest(
        name = "UIM Quest Cape",
        description = "Completes all quests on an Ultimate Ironman account",
        author = "YourName",
        version = 1.0,
        category = Category.QUEST
)
public class UIMQuestCape extends AbstractScript {
    private State currentState;
    private StateManager stateManager;

    @Override
    public void onStart() {
        Logger.log("Starting UIM Quest Cape bot");

        // Initialize the state manager
        stateManager = new StateManager();

        // Initialize with an idle state until we determine where we are in the progression
        currentState = new IdleState();

        // Determine initial state based on account progress
        determineInitialState();
    }

    @Override
    public int onLoop() {
        // Check if we have a valid current state
        if (currentState == null) {
            Logger.log("Current state is null, defaulting to idle state");
            currentState = new IdleState();
            return 1000;
        }

        // Log current state for debugging
        Logger.log("Current state: " + currentState.status());

        // Execute the current state
        boolean completed = currentState.execute();

        // If the state has completed its task, move to the next state
        if (completed && currentState.getNextState() != null) {
            Logger.log("State completed, moving to next state: " + currentState.getNextState().status());
            currentState = currentState.getNextState();
        } else if (completed) {
            Logger.log("State completed but no next state defined. Defaulting to idle.");
            currentState = new IdleState();
        }

        // Sleep between loops
        return 600; // Sleep for 600ms
    }

    /**
     * Determines the initial state based on the current progress of the account.
     * This checks completed quests, inventory items, skills, and location.
     */
    private void determineInitialState() {
        // For now, we'll always start with the early game progression
        // In the future, we can determine where in the sequence we should start
        // based on completed quests, skills, etc.

        Logger.log("Determining initial state based on account progress");

        // For testing, we'll start with the early game essentials
        EarlyGameEssentialsGroup earlyGameGroup = new EarlyGameEssentialsGroup();
        currentState = earlyGameGroup.getStartingState();

        Logger.log("Initial state set to: " + currentState.status());
    }

    @Override
    public void onExit() {
        Logger.log("UIM Quest Cape bot stopped");
    }
}