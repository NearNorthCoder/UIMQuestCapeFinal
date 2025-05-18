package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for traveling to Ape Atoll during Monkey Madness quest
 */
public class TravelToApeAtollState extends AbstractState {

    public TravelToApeAtollState(UIMQuestCape script) {
        super(script, "TravelToApeAtollState");
    }

    @Override
    public int execute() {
        Logger.log("Traveling to Ape Atoll - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}