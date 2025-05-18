package org.dreambot.uimquestcape.states.quests.spiritsofelid;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class SpiritsOfElidStartState extends AbstractState {
    
    public SpiritsOfElidStartState(UIMQuestCape script) {
        super(script, "SpiritsOfElidStartState");
    }
    
    @Override
    public int execute() {
        Logger.log("Starting Spirits of the Elid quest");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}