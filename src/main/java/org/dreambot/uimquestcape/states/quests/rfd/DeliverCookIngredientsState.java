package org.dreambot.uimquestcape.states.quests.rfd;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class DeliverCookIngredientsState extends AbstractState {

    public DeliverCookIngredientsState(UIMQuestCape script) {
        super(script, "DeliverCookIngredientsState");
    }

    @Override
    public int execute() {
        Logger.log("Delivering ingredients to Cook - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}