package org.dreambot.uimquestcape.states.wintertodt;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class CompleteWintertodtGamesState extends AbstractState {

    public CompleteWintertodtGamesState(UIMQuestCape script) {
        super(script, "CompleteWintertodtGamesState");
    }

    @Override
    public int execute() {
        Logger.log("Completing Wintertodt games - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}