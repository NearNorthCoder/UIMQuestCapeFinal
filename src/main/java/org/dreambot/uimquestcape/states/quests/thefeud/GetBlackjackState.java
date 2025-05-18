package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class GetBlackjackState extends AbstractState {

    public GetBlackjackState(UIMQuestCape script) {
        super(script, "GetBlackjackState");
    }

    @Override
    public int execute() {
        Logger.log("Getting blackjack");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}