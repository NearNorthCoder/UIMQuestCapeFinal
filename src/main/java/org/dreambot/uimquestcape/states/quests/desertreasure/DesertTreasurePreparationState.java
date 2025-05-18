package org.dreambot.uimquestcape.states.quests.desertreasure;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class DesertTreasurePreparationState extends AbstractState {
    
    public DesertTreasurePreparationState(UIMQuestCape script) {
        super(script, "DesertTreasurePreparationState");
    }
    
    @Override
    public int execute() {
        Logger.log("Preparing for Desert Treasure");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}