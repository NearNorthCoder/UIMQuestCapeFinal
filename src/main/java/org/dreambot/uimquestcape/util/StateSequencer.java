package org.dreambot.uimquestcape.util;

import java.util.ArrayList;
import java.util.List;
import org.dreambot.uimquestcape.State;
import org.dreambot.uimquestcape.UIMQuestCape;

// Control flow for state sequence
public class StateSequencer {
    private final UIMQuestCape script;
    private final List<String> completedStates = new ArrayList<>();
    
    public StateSequencer(UIMQuestCape script) {
        this.script = script;
    }
    
    public void markStateCompleted(String stateName) {
        completedStates.add(stateName);
    }
    
    public boolean isStateCompleted(String stateName) {
        return completedStates.contains(stateName);
    }
    
    public State determineNextState(State currentState) {
        // Logic to handle branching state paths based on conditions
        // This allows for more complex decision-making than simple linear progression
        
        return currentState.getNextState(); // Default is linear progression
    }
    
    // Methods to handle conditional progression
    public boolean shouldSkipState(String stateName) {
        // Determine if a state should be skipped based on other completed states
        return false; // Placeholder
    }
}