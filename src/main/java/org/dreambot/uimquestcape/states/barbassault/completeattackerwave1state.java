package org.dreambot.uimquestcape.states.barbassault;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for completing Wave 1 as an Attacker in Barbarian Assault
 */
public class CompleteAttackerWave1State extends AbstractState {
    
    private boolean waveStarted = false;
    private boolean waveCompleted = false;
    
    public CompleteAttackerWave1State(UIMQuestCape script) {
        super(script, "CompleteAttackerWave1State");
    }
    
    @Override
    public int execute() {
        // Check if we're in the correct role room
        if (!BarbarianAssaultHelper.isInRoleRoom("attacker")) {
            Logger.log("Not in Attacker room, need to join first");
            return 1000;
        }
        
        // Handle game over or wave complete
        if (handleEndOfWaveInterfaces()) {
            return 600;
        }
        
        // Check if wave is completed
        if (waveCompleted) {
            Logger.log("Wave 1 completed as Attacker");
            complete();
            return 600;
        }
        
        // Handle dialogues if active
        if (Dialogues.inDialogue()) {
            Dialogues.clickContinue();
            return 600;
        }
        
        // If wave hasn't started yet, wait for team formation
        if (!waveStarted) {
            return handleTeamFormation();
        }
        
        // Perform Attacker role duties
        return performAttackerDuties();
    }
    
    private int handleTeamFormation() {
        // Check if enough teammates have joined
        if (BarbarianAssaultHelper.hasTeamFormedForWave()) {
            Logger.log("Team has formed, starting wave");
            waveStarted = true;
            return 600;
        }
        
        Logger.log("Waiting for team to form");
        return 1000;
    }
    
    private int performAttackerDuties() {
        // Get the call (what weapon to use)
        String call = BarbarianAssaultHelper.getCallForRole("attacker");
        
        // Attack penance based on call
        if (call.isEmpty()) {
            Logger.log("No call visible yet");
            return 600;
        }
        
        Logger.log("Attacker call: " + call);
        
        // Attack penance fighters using appropriate style
        NPC penanceFighter = NPCs.closest(npc -> 
            npc != null && 
            npc.getName() != null && 
            npc.getName().contains("Penance Fighter") && 
            !npc.isInCombat() && 
            npc.hasAction("Attack"));
        
        if (penanceFighter != null) {
            String attackStyle = getAttackStyleForCall(call);
            
            // Set attack style based on call
            setAttackStyle(attackStyle);
            
            // Attack the penance fighter
            penanceFighter.interact("Attack");
            Sleep.sleepUntil(() -> Players.getLocal().isInCombat(), 3000);
        }
        
        return 600;
    }
    
    private String getAttackStyleForCall(String call) {
        // Convert call to attack style
        switch (call.toLowerCase()) {
            case "controlled":
                return "Controlled";
            case "accurate":
                return "Accurate";
            case "aggressive":
                return "Aggressive";
            case "defensive":
                return "Defensive";
            default:
                return "Controlled"; // Default
        }
    }
    
    private void setAttackStyle(String style) {
        // Implementation for setting attack style would go here
        // This would interact with the combat options tab
        Logger.log("Setting attack style to: " + style);
    }
    
    private boolean handleEndOfWaveInterfaces() {
        // Check for game over interface
        if (BarbarianAssaultHelper.isGameOver()) {
            Logger.log("Game over detected");
            BarbarianAssaultHelper.closeGameOverInterface();
            return true;
        }
        
        // Check for wave completion interface
        if (BarbarianAssaultHelper.isWaveCompleted()) {
            Logger.log("Wave completed");
            waveCompleted = true;
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean canExecute() {
        return BarbarianAssaultHelper.isInRoleRoom("attacker") && !waveCompleted;
    }
}
