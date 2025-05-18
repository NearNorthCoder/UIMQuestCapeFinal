package org.dreambot.uimquestcape;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the states for the UIM Quest Cape bot.
 * Provides utility methods for state transitions and tracking.
 */
public class StateManager {
    private Map<String, State> states = new HashMap<>();

    /**
     * Registers a state with a unique identifier.
     *
     * @param id The unique identifier for the state
     * @param state The state to register
     */
    public void registerState(String id, State state) {
        states.put(id, state);
    }

    /**
     * Gets a registered state by its identifier.
     *
     * @param id The identifier of the state to retrieve
     * @return The state, or null if no state with that id exists
     */
    public State getState(String id) {
        return states.get(id);
    }

    /**
     * Creates a transition between two states.
     *
     * @param fromId The identifier of the state to transition from
     * @param toId The identifier of the state to transition to
     */
    public void createTransition(String fromId, String toId) {
        State fromState = states.get(fromId);
        State toState = states.get(toId);

        if (fromState != null && toState != null) {
            fromState.setNextState(toState);
        }
    }

    /**
     * Creates a linear sequence of states.
     * Each state in the array will transition to the next one in sequence.
     *
     * @param stateIds An array of state identifiers in sequence
     */
    public void createSequence(String... stateIds) {
        for (int i = 0; i < stateIds.length - 1; i++) {
            createTransition(stateIds[i], stateIds[i + 1]);
        }
    }
}