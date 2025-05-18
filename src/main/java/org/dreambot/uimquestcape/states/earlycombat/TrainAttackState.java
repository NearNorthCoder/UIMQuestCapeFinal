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
 * State for training Attack skill on barbarians
 */
public class TrainAttackState extends AbstractState {
    
    private static final Area BARBARIAN_VILLAGE_AREA = new Area(
            new Tile(3072, 3433, 0),
            new Tile(3089, 3415, 0)
    );
    
    private static final int TARGET_ATTACK_LEVEL = 15;
    
    public TrainAttackState(UIMQuestCape script) {
        super(script, "TrainAttackState");
    }
    
    @Override
    public int execute() {
        // Check if we've reached target attack level
        if (Skills.getRealLevel(Skills.ATTACK) >= TARGET_ATTACK_LEVEL) {
            Logger.log("Reached target Attack level: " + TARGET_ATTACK_LEVEL);
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
        
        // Set combat style to Attack
        setCombatStyle(0); // 0 = Accurate/Attack style
        
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
        return Skills.getRealLevel(Skills.ATTACK) < TARGET_ATTACK_LEVEL;
    }
}