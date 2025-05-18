package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for getting the monkey amulet mould during Monkey Madness quest
 */
public class GetMonkeyAmuletMouldState extends AbstractState {

    public GetMonkeyAmuletMouldState(UIMQuestCape script) {
        super(script, "GetMonkeyAmuletMouldState");
    }

    @Override
    public int execute() {
        Logger.log("Getting monkey amulet mould - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}