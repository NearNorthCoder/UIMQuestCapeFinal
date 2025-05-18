package org.dreambot.uimquestcape.states.ardougnediary;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TeleportToEssenceMineState extends AbstractState {

    public TeleportToEssenceMineState(UIMQuestCape script) {
        super(script, "TeleportToEssenceMineState");
    }

    @Override
    public int execute() {
        Logger.log("Teleporting to Essence Mine - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}