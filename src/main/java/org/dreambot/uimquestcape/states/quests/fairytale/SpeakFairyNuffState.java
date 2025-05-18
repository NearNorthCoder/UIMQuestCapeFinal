package org.dreambot.uimquestcape.states.quests.fairytale;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class SpeakFairyNuffState extends AbstractState {
    
    public SpeakFairyNuffState(UIMQuestCape script) {
        super(script, "SpeakFairyNuffState");
    }
    
    @Override
    public int execute() {
        Logger.log("Speaking to Fairy Nuff - placeholder implementation");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}