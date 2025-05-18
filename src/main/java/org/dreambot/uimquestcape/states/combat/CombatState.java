package org.dreambot.uimquestcape.states.combat;

import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.api.wrappers.interactive.NPC;

/**
 * Base class for combat-related states that handles common combat logic
 */
public abstract class CombatState extends AbstractState {
    protected String targetName;
    protected int targetId;
    protected int eatAtHealthPercent = 50;
    protected boolean usePrayer = false;
    protected String prayerType = "PROTECT_FROM_MELEE";
    
    public CombatState(UIMQuestCape script, String name) {
        super(script, name);
    }
    
    @Override
    public int execute() {
        // Check if we're in combat first
        if (Players.getLocal().isInCombat() && !needsToEat()) {
            return handleCombat();
        }
        
        // If we need to eat, prioritize that
        if (needsToEat()) {
            return handleEating();
        }
        
        // If we're not in combat, find target
        return findAndAttackTarget();
    }
    
    /**
     * Handles behavior while in combat
     */
    protected int handleCombat() {
        // Default implementation - can be overridden by subclasses
        // For most basic combat, just wait while in combat
        return 600;
    }
    
    /**
     * Finds and attacks the target NPC
     */
    protected int findAndAttackTarget() {
        // Find target by name or ID depending on what's set
        if (targetName != null && !targetName.isEmpty()) {
            NPC target = NPCs.closest(npc ->
                targetName.equals(npc.getName()) && 
                !npc.isInCombat() && 
                npc.getHealthPercent() > 0
            );
            
            if (target != null) {
                Logger.log("Attacking " + targetName);
                if (target.interact("Attack")) {
                    Sleep.sleepUntil(() -> Players.getLocal().isInCombat(), 5000);
                }
            } else {
                Logger.log("Could not find target: " + targetName);
            }
        } else if (targetId > 0) {
            NPC target = NPCs.closest(npc ->
                npc.getID() == targetId && 
                !npc.isInCombat() && 
                npc.getHealthPercent() > 0
            );
            
            if (target != null) {
                Logger.log("Attacking NPC with ID: " + targetId);
                if (target.interact("Attack")) {
                    Sleep.sleepUntil(() -> Players.getLocal().isInCombat(), 5000);
                }
            } else {
                Logger.log("Could not find target with ID: " + targetId);
            }
        }
        
        return 1000;
    }
    
    /**
     * Checks if player needs to eat
     */
    protected boolean needsToEat() {
        return Players.getLocal().getHealthPercent() <= eatAtHealthPercent;
    }
    
    /**
     * Handles eating food
     */
    protected int handleEating() {
        // Implementation would find food in inventory and eat it
        Logger.log("Need to eat food");
        // Placeholder for actual food eating implementation
        return 600;
    }
    
    /**
     * Sets up prayer if needed
     */
    protected void setupPrayer() {
        if (usePrayer && !isCorrectedPrayerActive()) {
            activatePrayer();
        }
    }
    
    /**
     * Checks if the correct prayer is active
     */
    protected boolean isCorrectedPrayerActive() {
        // Implementation would check if the specified prayer is active
        return false; // Placeholder
    }
    
    /**
     * Activates the appropriate prayer
     */
    protected void activatePrayer() {
        // Implementation would activate the specified prayer
        Logger.log("Activating prayer: " + prayerType);
    }
}