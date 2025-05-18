package org.dreambot.uimquestcape.states.earlyessentials;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class AcquireSteelPlatebodyState extends AbstractState {

    private static final Area STEEL_PLATEBODY_AREA = new Area(
            new Tile(3092, 3573, 0),
            new Tile(3096, 3567, 0)
    );

    public AcquireSteelPlatebodyState(UIMQuestCape script) {
        super(script, "AcquireSteelPlatebodyState");
    }

    @Override
    public int execute() {
        // Check if we already have a steel platebody
        if (Inventory.contains("Steel platebody")) {
            Logger.log("Already have a steel platebody");
            complete();
            return 600;
        }

        // Travel to area if not there
        if (!STEEL_PLATEBODY_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to steel platebody spawn");
            Walking.walk(STEEL_PLATEBODY_AREA.getCenter());
            return 1000;
        }

        // Look for the steel platebody
        GameObject steelPlatebody = GameObjects.closest(obj ->
                obj != null &&
                        obj.getName() != null &&
                        obj.getName().equals("Steel platebody") &&
                        obj.hasAction("Take")
        );

        if (steelPlatebody != null) {
            Logger.log("Taking steel platebody");
            steelPlatebody.interact("Take");
            Sleep.sleepUntil(() -> Inventory.contains("Steel platebody"), 5000);

            if (Inventory.contains("Steel platebody")) {
                complete();
            }
        }

        return 600;
    }

    @Override
    public boolean canExecute() {
        return !Inventory.contains("Steel platebody");
    }
}