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
        Logger.log("Current state: " + currentState.getName());  // Change status() to getName()

        // Execute the current state and get the sleep time
        int sleepTime = currentState.execute();  // Store as int, not boolean

        // If the state has completed its task, move to the next state
        if (currentState.isCompleted() && currentState.getNextState() != null) {
            Logger.log("State completed, moving to next state: " + currentState.getNextState().getName());  // Change status() to getName()
            currentState = currentState.getNextState();
        } else if (currentState.isCompleted()) {
            Logger.log("State completed but no next state defined. Defaulting to idle.");
            currentState = new IdleState();
        }

        // Sleep between loops
        return sleepTime;  // Return the sleep time from execute()
    }

    /**
     * Determines the initial state based on the current progress of the account.
     * This checks completed quests, inventory items, skills, and location.
     */
    private void determineInitialState() {
        // For testing, we'll start with the early game essentials
        Logger.log("Determining initial state based on account progress");

        // Pass "this" as the argument to the constructor
        EarlyGameEssentialsGroup earlyGameGroup = new EarlyGameEssentialsGroup(this);
        currentState = earlyGameGroup.getInitialState();

        Logger.log("Initial state set to: " + currentState.getName());
    }

    @Override
    public void onExit() {
        Logger.log("UIM Quest Cape bot stopped");
    }
}