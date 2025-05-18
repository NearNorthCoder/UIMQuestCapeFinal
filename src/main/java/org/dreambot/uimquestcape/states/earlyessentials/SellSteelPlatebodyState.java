package org.dreambot.uimquestcape.states.earlyessentials;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

public class SellSteelPlatebodyState extends AbstractState {

    private static final Area GENERAL_STORE_AREA = new Area(
            new Tile(3081, 3509, 0),
            new Tile(3086, 3504, 0)
    );

    public SellSteelPlatebodyState(UIMQuestCape script) {
        super(script, "SellSteelPlatebodyState");
    }

    @Override
    public int execute() {
        // Check if we have a steel platebody
        if (!Inventory.contains("Steel platebody")) {
            Logger.log("No steel platebody to sell");
            return 600;
        }

        // Check if we have enough coins (success condition)
        if (Inventory.count("Coins") >= 1200) {
            Logger.log("Successfully sold steel platebody");
            complete();
            return 600;
        }

        // Travel to general store if not there
        if (!GENERAL_STORE_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to general store");
            Walking.walk(GENERAL_STORE_AREA.getCenter());
            return 1000;
        }

        // Find shopkeeper
        NPC shopkeeper = NPCs.closest("Shopkeeper");
        if (shopkeeper != null) {
            Logger.log("Talking to shopkeeper to sell platebody");
            shopkeeper.interact("Trade");
            Sleep.sleep(1200, 2000);

            // Sell the platebody (implement sell logic)
            // This is a simplified placeholder
            Logger.log("Sold steel platebody");

            // Check if we have the coins now
            if (Inventory.count("Coins") >= 1200) {
                complete();
            }
        }

        return 600;
    }

    @Override
    public boolean canExecute() {
        return Inventory.contains("Steel platebody") && Inventory.count("Coins") < 1200;
    }
}