package org.dreambot.uimquestcape.states.quests.fairytale;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class SpeakFairyGodfatherState extends AbstractState {
    
    public SpeakFairyGodfatherState(UIMQuestCape script) {
        super(script, "SpeakFairyGodfatherState");
    }
    
    @Override
    public int execute() {
        Logger.log("Speaking to Fairy Godfather - placeholder implementation");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}