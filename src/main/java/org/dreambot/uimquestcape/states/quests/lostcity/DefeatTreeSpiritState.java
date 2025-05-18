package org.dreambot.uimquestcape.states.quests.lostcity;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class DefeatTreeSpiritState extends AbstractState {
    
    public DefeatTreeSpiritState(UIMQuestCape script) {
        super(script, "DefeatTreeSpiritState");
    }
    
    @Override
    public int execute() {
        Logger.log("Defeating Tree Spirit - placeholder implementation");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}