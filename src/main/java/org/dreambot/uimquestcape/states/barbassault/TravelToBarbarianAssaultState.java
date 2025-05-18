package org.dreambot.uimquestcape.states.barbassault;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.NavigationHelper;

/**
 * State for traveling to the Barbarian Assault minigame
 */
public class TravelToBarbarianAssaultState extends AbstractState {
    
    private static final Area BARBARIAN_ASSAULT_AREA = new Area(
            new Tile(2520, 3573, 0),
            new Tile(2565, 3538, 0)
    );
    
    private final NavigationHelper navigation;
    
    public TravelToBarbarianAssaultState(UIMQuestCape script) {
        super(script, "TravelToBarbarianAssaultState");
        this.navigation = new NavigationHelper(script);
    }
    
    @Override
    public int execute() {
        // Check if we're already at Barbarian Assault
        if (BARBARIAN_ASSAULT_AREA.contains(Players.getLocal())) {
            Logger.log("Reached Barbarian Assault minigame area");
            complete();
            return 600;
        }
        
        // Navigate to Barbarian Assault
        Logger.log("Traveling to Barbarian Assault");
        
        // Check if we have a teleport method (games necklace)
        if (hasGamesNecklace()) {
            return teleportToBarbarianAssault();
        } else {
            // Walk using web walking
            navigation.webWalk(BARBARIAN_ASSAULT_AREA.getCenter());
            Sleep.sleepUntil(() -> BARBARIAN_ASSAULT_AREA.contains(Players.getLocal()), 30000);
            return 1000;
        }
    }
    
    private boolean hasGamesNecklace() {
        return false; // Placeholder - would check inventory for games necklace
    }
    
    private int teleportToBarbarianAssault() {
        // Placeholder - would use games necklace to teleport
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return !BARBARIAN_ASSAULT_AREA.contains(Players.getLocal());
    }
}