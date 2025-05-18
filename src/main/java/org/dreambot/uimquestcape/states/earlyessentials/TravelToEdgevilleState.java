package org.dreambot.uimquestcape.states.earlyessentials;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class TravelToEdgevilleState extends AbstractState {

    private static final Area EDGEVILLE_AREA = new Area(
            new Tile(3081, 3502, 0),
            new Tile(3098, 3487, 0)
    );

    public TravelToEdgevilleState(UIMQuestCape script) {
        super(script, "TravelToEdgevilleState");
    }

    @Override
    public int execute() {
        // Check if we're already at Edgeville
        if (EDGEVILLE_AREA.contains(Players.getLocal())) {
            Logger.log("Reached Edgeville");
            complete();
            return 600;
        }

        // Walk to Edgeville
        Logger.log("Walking to Edgeville");
        Walking.walkExact(EDGEVILLE_AREA.getCenter());

        // Wait until arrived or stopped moving
        Sleep.sleepUntil(() ->
                        EDGEVILLE_AREA.contains(Players.getLocal()) ||
                                !Players.getLocal().isMoving(),
                10000
        );

        return 1000;
    }

    @Override
    public boolean canExecute() {
        return !EDGEVILLE_AREA.contains(Players.getLocal());
    }
}