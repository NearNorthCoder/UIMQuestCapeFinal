package org.dreambot.uimquestcape.states.magic;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class HighAlchemyUnlockState extends AbstractState {

    public HighAlchemyUnlockState(UIMQuestCape script) {
        super(script, "HighAlchemyUnlockState");
    }

    @Override
    public int execute() {
        Logger.log("Unlocking High Alchemy - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}