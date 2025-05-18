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
     * Sets the next state that should be executed after this one completes.
     * @param nextState The next State instance to execute.
     */
    void setNextState(State nextState);
    
    /**
     * Checks if this state can execute based on current conditions
     * @return true if the state can execute
     */
    boolean canExecute();
}