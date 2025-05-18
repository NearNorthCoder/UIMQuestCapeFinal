package org.dreambot.uimquestcape.states.quests.rfd;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class DeliverCakeState extends AbstractState {

    public DeliverCakeState(UIMQuestCape script) {
        super(script, "DeliverCakeState");
    }

    @Override
    public int execute() {
        Logger.log("Delivering cake to Dwarf - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}