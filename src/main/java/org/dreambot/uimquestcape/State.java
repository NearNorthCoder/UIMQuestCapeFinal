package org.dreambot.uimquestcape;

/**
 * Interface for all states in the state machine
 */
public interface State {
    /**
     * Gets the name of this state
     * @return state name
     */
    String getName();
    
    /**
     * Executes the state logic
     * @return milliseconds to sleep before next execution
     */
    int execute();
    
    /**
     * Checks if this state is completed
     * @return true if state is completed
     */
    boolean isCompleted();
    
    /**
     * Gets the next state to transition to after this one completes
     * @return next state
     */
    State getNextState();
    
    /**
     * Checks if this state can execute based on current conditions
     * @return true if the state can execute
     */
    boolean canExecute();
}