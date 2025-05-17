package org.dreambot.uimquestcape.util;

import java.util.ArrayList;
import java.util.List;
import org.dreambot.uimquestcape.State;

// Group related states together for organization
public class StateGroup {
    private final String groupName;
    private final List<State> states = new ArrayList<>();
    
    public StateGroup(String groupName) {
        this.groupName = groupName;
    }
    
    public void addState(State state) {
        states.add(state);
    }
    
    public List<State> getStates() {
        return states;
    }
    
    public String getGroupName() {
        return groupName;
    }
}