package org.dreambot.uimquestcape.states.quests.monkeymadness;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for getting the monkey greegree during Monkey Madness quest
 */
public class GetMonkeyGreegreeState extends AbstractState {

    public GetMonkeyGreegreeState(UIMQuestCape script) {
        super(script, "GetMonkeyGreegreeState");
    }

    @Override
    public int execute() {
        Logger.log("Getting monkey greegree - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}