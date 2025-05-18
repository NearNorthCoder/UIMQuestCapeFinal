package org.dreambot.uimquestcape.states.quests.fairytale;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class ReadBookState extends AbstractState {
    
    public ReadBookState(UIMQuestCape script) {
        super(script, "ReadBookState");
    }
    
    @Override
    public int execute() {
        Logger.log("Reading book for Fairy Tale Part II - placeholder implementation");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}