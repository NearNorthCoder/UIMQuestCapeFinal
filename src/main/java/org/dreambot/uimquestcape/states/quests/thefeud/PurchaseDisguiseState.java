package org.dreambot.uimquestcape.states.quests.thefeud;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class PurchaseDisguiseState extends AbstractState {

    public PurchaseDisguiseState(UIMQuestCape script) {
        super(script, "PurchaseDisguiseState");
    }

    @Override
    public int execute() {
        Logger.log("Purchasing disguise for The Feud quest");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}