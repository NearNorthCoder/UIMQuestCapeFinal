package org.dreambot.uimquestcape.states.magic;

import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TrainOnZamorakMagesState extends AbstractState {

    public TrainOnZamorakMagesState(UIMQuestCape script) {
        super(script, "TrainOnZamorakMagesState");
    }

    @Override
    public int execute() {
        Logger.log("Training on Zamorak mages - placeholder implementation");
        complete();
        return 1000;
    }

    @Override
    public boolean canExecute() {
        return true;
    }
}