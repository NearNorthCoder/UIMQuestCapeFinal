package org.dreambot.uimquestcape.states.earlyessentials;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToVarrockState extends AbstractState {

    private static final Area VARROCK_AREA = new Area(
            new Tile(3206, 3433, 0),
            new Tile(3228, 3410, 0)
    );

    public TravelToVarrockState(UIMQuestCape script) {
        super(script, "TravelToVarrockState");
    }

    @Override
    public int execute() {
        // Check if we're already at Varrock
        if (VARROCK_AREA.contains(Players.getLocal())) {
            Logger.log("Reached Varrock");
            complete();
            return 600;
        }

        // Walk to Varrock
        Logger.log("Walking to Varrock");
        Walking.walkExact(VARROCK_AREA.getCenter());

        // Wait until arrived or stopped moving
        Sleep.sleepUntil(() ->
                        VARROCK_AREA.contains(Players.getLocal()) ||
                                !Players.getLocal().isMoving(),
                10000
        );

        return 1000;
    }

    @Override
    public boolean canExecute() {
        return !VARROCK_AREA.contains(Players.getLocal());
    }
}