package org.dreambot.uimquestcape.states.earlycombat;

import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for training Strength skill on barbarians
 */
public class TrainStrengthState extends AbstractState {
    
    private static final Area BARBARIAN_VILLAGE_AREA = new Area(
            new Tile(3072, 3433, 0),
            new Tile(3089, 3415, 0)
    );
    
    private static final int TARGET_STRENGTH_LEVEL = 15;
    
    public TrainStrengthState(UIMQuestCape script) {
        super(script, "TrainStrengthState");
    }
    
    @Override
    public int execute() {
        // Check if we've reached target strength level
        if (Skills.getRealLevel(Skills.STRENGTH) >= TARGET_STRENGTH_LEVEL) {
            Logger.log("Reached target Strength level: " + TARGET_STRENGTH_LEVEL);
            complete();
            return 600;
        }
        
        // Ensure we're at Barbarian Village
        if (!BARBARIAN_VILLAGE_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to Barbarian Village");
            Walking.walkExact(BARBARIAN_VILLAGE_AREA.getCenter());
            return 1000;
        }
        
        // If we're in combat, wait
        if (Players.getLocal().isInCombat()) {
            Logger.log("In combat, waiting...");
            return 1000;
        }
        
        // If low on health, eat food
        if (Players.getLocal().getHealthPercent() < 50) {
            return handleEating();
        }
        
        // Set combat style to Strength
        setCombatStyle(1); // 1 = Aggressive/Strength style
        
        // Find a barbarian to attack
        NPC barbarian = NPCs.closest(npc -> 
            npc != null && 
            npc.getName() != null && 
            npc.getName().equals("Barbarian") && 
            !npc.isInCombat() && 
            npc.getHealthPercent() > 0
        );
        
        if (barbarian != null) {
            Logger.log("Attacking barbarian");
            barbarian.interact("Attack");
            Sleep.sleepUntil(() -> Players.getLocal().isInCombat(), 5000);
        }
        
        return 600;
    }
    
    private void setCombatStyle(int style) {
        // Only change if necessary
        if (Combat.getAttackStyle() != style) {
            Combat.setAttackStyle(style);
        }
    }
    
    private int handleEating() {
        Logger.log("Health low, need to eat food");
        // Placeholder for eating food
        // In a real implementation, this would find food in inventory and eat it
        return 600;
    }
    
    @Override
    public boolean canExecute() {
        return Skills.getRealLevel(Skills.STRENGTH) < TARGET_STRENGTH_LEVEL;
    }
}