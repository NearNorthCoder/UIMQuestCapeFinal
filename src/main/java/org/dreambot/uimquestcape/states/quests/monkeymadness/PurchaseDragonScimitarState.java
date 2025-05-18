package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for purchasing dragon scimitar after Monkey Madness quest
 */
public class PurchaseDragonScimitarState extends AbstractState {

    public PurchaseDragonScimitarState(UIMQuestCape script) {
        super(script, "PurchaseDragonScimitarState");
    }

    @Override
    public int execute() {
        Logger.log("Purchasing dragon scimitar - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}