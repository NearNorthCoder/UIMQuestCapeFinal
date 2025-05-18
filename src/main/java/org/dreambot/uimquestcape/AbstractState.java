package org.dreambot.uimquestcape;

import org.dreambot.api.utilities.Logger;

// Abstract base class implementing common state functionality
public abstract class AbstractState implements State {
    protected final UIMQuestCape script;
    protected String name;
    protected State nextState;
    protected boolean completed = false;

    public AbstractState(UIMQuestCape script, String name) {
        this.script = script;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    
    public State getNextState() {
        return nextState;
    }

    // Most states can execute by default
    @Override
    public boolean canExecute() {
        return true;
    }

    // Set next state - useful for dynamic state transitions
    public void setNextState(State nextState) {
        this.nextState = nextState;
    }

    // Mark this state as completed
    protected void complete() {
        this.completed = true;
        Logger.log("Completed state: " + getName());
    }
}