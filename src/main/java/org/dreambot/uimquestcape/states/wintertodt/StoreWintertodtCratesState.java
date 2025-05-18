package org.dreambot.uimquestcape.states.wintertodt;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class StoreWintertodtCratesState extends AbstractState {

    public StoreWintertodtCratesState(UIMQuestCape script) {
        super(script, "StoreWintertodtCratesState");
    }

    @Override
    public int execute() {
        Logger.log("Storing Wintertodt crates - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}