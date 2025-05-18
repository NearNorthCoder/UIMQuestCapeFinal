package org.dreambot.uimquestcape.states.quests.lostcity;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class MakeDramenStaffState extends AbstractState {
    
    public MakeDramenStaffState(UIMQuestCape script) {
        super(script, "MakeDramenStaffState");
    }
    
    @Override
    public int execute() {
        Logger.log("Making Dramen Staff - placeholder implementation");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}