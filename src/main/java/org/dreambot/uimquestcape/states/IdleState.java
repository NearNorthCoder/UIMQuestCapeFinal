package org.dreambot.uimquestcape.states;

import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.uimquestcape.AbstractState;

/**
 * A fallback state that does nothing but wait.
 * Used when no other state is appropriate or when waiting for user input.
 */
public class IdleState extends AbstractState {

    @Override
    public boolean execute() {
        Logger.log("Idle state - waiting for next action");
        Sleep.sleep(2000, 3000); // Sleep for 2-3 seconds
        return false; // Always return false to stay in this state until manually changed
    }

    @Override
    public String status() {
        return "Idle - waiting for instructions";
    }
}