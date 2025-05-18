package org.dreambot.uimquestcape.states.quests.lostcity;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class LostCityCompleteState extends AbstractState {
    
    public LostCityCompleteState(UIMQuestCape script) {
        super(script, "LostCityCompleteState");
    }
    
    @Override
    public int execute() {
        Logger.log("Completing Lost City quest - placeholder implementation");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}