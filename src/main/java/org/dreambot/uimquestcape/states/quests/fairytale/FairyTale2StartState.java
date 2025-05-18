package org.dreambot.uimquestcape.states.quests.fairytale;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class FairyTale2StartState extends AbstractState {
    
    public FairyTale2StartState(UIMQuestCape script) {
        super(script, "FairyTale2StartState");
    }
    
    @Override
    public int execute() {
        Logger.log("Starting Fairy Tale Part II - placeholder implementation");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}