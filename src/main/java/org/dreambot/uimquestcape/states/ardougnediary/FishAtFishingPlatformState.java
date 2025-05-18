package org.dreambot.uimquestcape.states.ardougnediary;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class FishAtFishingPlatformState extends AbstractState {

    public FishAtFishingPlatformState(UIMQuestCape script) {
        super(script, "FishAtFishingPlatformState");
    }

    @Override
    public int execute() {
        Logger.log("Fishing at Fishing Platform - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}