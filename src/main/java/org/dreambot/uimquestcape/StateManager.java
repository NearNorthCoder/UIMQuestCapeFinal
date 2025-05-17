package org.dreambot.uimquestcape;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import org.dreambot.api.utilities.Logger;

// Manages all state transitions and state retrieval
public class StateManager {
    private final UIMQuestCape script;
    private State currentState;
    private Map<String, State> allStates = new HashMap<>();
    private Stack<State> stateHistory = new Stack<>();

    public StateManager(UIMQuestCape script) {
        this.script = script;
    }

    public void registerState(State state) {
        allStates.put(state.getName(), state);
    }

    public State getStateByName(String name) {
        return allStates.get(name);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State newState) {
        if (newState == null) {
            Logger.error("Attempted to set null state");
            return;
        }

        if (currentState != null) {
            stateHistory.push(currentState);
        }

        currentState = newState;
        Logger.log("Current state: " + currentState.getName());
    }

    public State getPreviousState() {
        return stateHistory.isEmpty() ? null : stateHistory.peek();
    }

    public void revertToPreviousState() {
        if (!stateHistory.isEmpty()) {
            currentState = stateHistory.pop();
            Logger.log("Reverted to state: " + currentState.getName());
        }
    }
}