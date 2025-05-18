package org.dreambot.uimquestcape.states.slayer;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class CreateSlayerHelmetState extends AbstractState {

    public CreateSlayerHelmetState(UIMQuestCape script) {
        super(script, "CreateSlayerHelmetState");
    }

    @Override
    public int execute() {
        Logger.log("Creating Slayer Helmet - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}