package org.dreambot.uimquestcape.states.quests.lostcity;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class LostCityStartState extends AbstractState {
    
    public LostCityStartState(UIMQuestCape script) {
        super(script, "LostCityStartState");
    }
    
    @Override
    public int execute() {
        Logger.log("Starting Lost City quest - placeholder implementation");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}