package org.dreambot.uimquestcape.states.wintertodt;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class EnterWintertodtState extends AbstractState {

    public EnterWintertodtState(UIMQuestCape script) {
        super(script, "EnterWintertodtState");
    }

    @Override
    public int execute() {
        Logger.log("Entering Wintertodt arena - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}