package org.dreambot.uimquestcape.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.State;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * Group related states together for organization and management
 */
public abstract class StateGroup implements Serializable {
    private final String groupName;
    private final List<State> states = new ArrayList<>();
    private final Map<String, State> stateMap = new HashMap<>();
    private transient UIMQuestCape script;
    private boolean completed = false;

    public StateGroup(UIMQuestCape script, String groupName) {
        this.script = script;
        this.groupName = groupName;
    }

    /**
     * Adds a state to this group
     * @param state The state to add
     */
    public void addState(State state) {
        states.add(state);
        stateMap.put(state.getName(), state);
    }

    /**
     * Gets all states in this group
     * @return List of states
     */
    public List<State> getStates() {
        return states;
    }

    /**
     * Gets a state by name
     * @param name Name of the state
     * @return The state or null if not found
     */
    public State getStateByName(String name) {
        return stateMap.get(name);
    }

    /**
     * Gets the group name
     * @return Group name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the script reference (needed after deserialization)
     * @param script The script instance
     */
    public void setScript(UIMQuestCape script) {
        this.script = script;
    }

    /**
     * Gets the script reference
     * @return The script instance
     */
    public UIMQuestCape getScript() {
        return script;
    }

    /**
     * Gets the initial state for this group
     * @return The first state to execute in this group
     */
    public abstract State getInitialState();

    /**
     * Checks if this group has been completed
     * @return true if group is completed
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Marks this group as completed
     */
    public void markCompleted() {
        this.completed = true;
        Logger.log("Completed state group: " + getGroupName());
    }

    /**
     * Checks if requirements are met to begin this group
     * @return true if requirements are met
     */
    public abstract boolean requirementsMet();

    /**
     * Determines the current state within this group based on game conditions
     * @return The current state or initial state if cannot be determined
     */
    public abstract State determineCurrentState();
}