package org.dreambot.uimquestcape.states.quests.lostcity;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class FindDramenTreeState extends AbstractState {
    
    public FindDramenTreeState(UIMQuestCape script) {
        super(script, "FindDramenTreeState");
    }
    
    @Override
    public int execute() {
        Logger.log("Finding Dramen Tree - placeholder implementation");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}