package org.dreambot.uimquestcape.states.quests.fairytale;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class CollectIngredientsState extends AbstractState {
    
    public CollectIngredientsState(UIMQuestCape script) {
        super(script, "CollectIngredientsState");
    }
    
    @Override
    public int execute() {
        Logger.log("Collecting ingredients for Fairy Tale Part I - placeholder implementation");
        complete();
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return true;
    }
}