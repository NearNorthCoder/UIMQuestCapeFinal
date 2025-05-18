package org.dreambot.uimquestcape.states.quests.fairytale;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class UnlockFairyRingsState extends AbstractState {
    
    public UnlockFairyRingsState(UIMQuestCape script) {
        super(script, "UnlockFairyRingsState");
    }
    
    @Override
    public int execute() {
        Logger.log("Unlocking fairy rings - placeholder implementation");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}