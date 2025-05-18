package org.dreambot.uimquestcape.states.ardougnediary;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class ClaimArdougneCloak1State extends AbstractState {

    public ClaimArdougneCloak1State(UIMQuestCape script) {
        super(script, "ClaimArdougneCloak1State");
    }

    @Override
    public int execute() {
        Logger.log("Claiming Ardougne Cloak 1 - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}