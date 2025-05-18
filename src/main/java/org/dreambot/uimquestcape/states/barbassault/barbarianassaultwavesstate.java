package org.dreambot.uimquestcape.states.barbassault;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.utilities.Logger;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * Base state for completing Waves 3-10 in all roles
 * This can be extended to create states for specific waves
 */
public abstract class BarbarianAssaultWavesState extends AbstractState {

    protected String[] roles = {"attacker", "defender", "collector", "healer"};
    protected String currentRole = roles[0];
    protected boolean waveStarted = false;
    protected boolean waveCompleted = false;
    protected int completedRoles = 0;
    protected final int waveNumber;
    
    public BarbarianAssaultWavesState(UIMQuestCape script, String name, int waveNumber) {
        super(script, name);
        this.waveNumber = waveNumber;
    }
    
    @Override
    public int execute() {
        // Check if all roles completed
        if (completedRoles >= roles.length) {
            Logger.log("All roles completed for Wave " + waveNumber);
            complete();
            return 600;
        }
        
        // Check if we've completed the current role
        if (waveCompleted) {
            completedRoles++;
            waveCompleted = false;
            waveStarted = false;
            
            // Move to next role
            int nextRoleIndex = 0;
            for (int i = 0; i < roles.length; i++) {
                if (roles[i].equals(currentRole)) {
                    nextRoleIndex = (i + 1) % roles.length;
                    break;
                }
            }
            currentRole = roles[nextRoleIndex];
            
            if (completedRoles >= roles.length) {
                Logger.log("All roles completed for Wave " + waveNumber);
                complete();
                return 600;
            }
            
            return joinNewRole(currentRole);
        }
        
        // Check if we're in the right role room
        if (!BarbarianAssaultHelper.isInRoleRoom(currentRole)) {
            return joinNewRole(currentRole);
        }
        
        // Handle game over or wave complete
        if (handleEndOfWaveInterfaces()) {
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
        
        // Perform role-specific duties
        switch (currentRole) {
            case "attacker":
                return performAttackerDuties();
            case "defender":
                return performDefenderDuties();
            case "collector":
                return performCollectorDuties();
            case "healer":
                return performHealerDuties();
            default:
                return 600;
        }
    }
    
    protected int joinNewRole(String role) {
        Logger.log("Joining " + role + " role for Wave " + waveNumber);
        
        // First exit room if in another role
        if (BarbarianAssaultHelper.isInRoleRoom("attacker") ||
            BarbarianAssaultHelper.isInRoleRoom("defender") ||
            BarbarianAssaultHelper.isInRoleRoom("collector") ||
            BarbarianAssaultHelper.isInRoleRoom("healer")) {
            // Find exit ladder
            Logger.log("Exiting current role room");
            // Exit room logic would go here
            return 1000;
        }
        
        // If in lobby, join new role
        if (BarbarianAssaultHelper.isInLobby()) {
            if (BarbarianAssaultHelper.enterRoleRoom(role)) {
                Logger.log("Entered " + role + " room");
                return 1000;
            }
        }
        
        return 600;
    }
    
    protected int handleTeamFormation() {
        // Check if enough teammates have joined
        if (BarbarianAssaultHelper.hasTeamFormedForWave()) {
            Logger.log("Team has formed for Wave " + waveNumber + ", starting as " + currentRole);
            waveStarted = true;
            return 600;
        }
        
        Logger.log("Waiting for team to form");
        return 1000;
    }
    
    // Role-specific duty methods
    protected abstract int performAttackerDuties();
    protected abstract int performDefenderDuties();
    protected abstract int performCollectorDuties();
    protected abstract int performHealerDuties();
    
    protected boolean handleEndOfWaveInterfaces() {
        // Check for game over interface
        if (BarbarianAssaultHelper.isGameOver()) {
            Logger.log("Game over detected");
            BarbarianAssaultHelper.closeGameOverInterface();
            return true;
        }
        
        // Check for wave completion interface
        if (BarbarianAssaultHelper.isWaveCompleted()) {
            Logger.log("Wave " + waveNumber + " completed as " + currentRole);
            waveCompleted = true;
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean canExecute() {
        return completedRoles < roles.length;
    }
}
