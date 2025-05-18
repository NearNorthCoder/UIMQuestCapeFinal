package org.dreambot.uimquestcape.states.quests.desertreasure;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class DeathPileRequiredItemsState extends AbstractState {
    
    public DeathPileRequiredItemsState(UIMQuestCape script) {
        super(script, "DeathPileRequiredItemsState");
    }
    
    @Override
    public int execute() {
        Logger.log("Creating death pile for required items");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}