package org.dreambot.uimquestcape.states.barbassault;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * State for joining the Collector role in Barbarian Assault
 */
public class JoinCollectorRoleState extends AbstractState {
    
    private static final Area BARBARIAN_ASSAULT_LOBBY = new Area(
            new Tile(2525, 3595, 0),
            new Tile(2540, 3565, 0)
    );
    
    private boolean roleSelected = false;
    
    public JoinCollectorRoleState(UIMQuestCape script) {
        super(script, "JoinCollectorRoleState");
    }
    
    @Override
    public int execute() {
        // Check if we've already selected the collector role
        if (roleSelected) {
            Logger.log("Collector role already selected");
            complete();
            return 600;
        }
        
        // Check if in the lobby area
        if (!BARBARIAN_ASSAULT_LOBBY.contains(Players.getLocal())) {
            Logger.log("Walking to Barbarian Assault lobby");
            return walkToLobby();
        }
        
        // Handle dialogues if open
        if (Dialogues.inDialogue()) {
            return handleDialogue();
        }
        
        // Find and interact with Collector role NPC
        NPC collectorCaptain = NPCs.closest(npc -> 
            npc != null && 
            npc.getName() != null && 
            npc.getName().contains("Captain") && 
            npc.hasAction("Talk-to") &&
            npc.getOverheadText() != null &&
            npc.getOverheadText().contains("Collector")
        );
        
        if (collectorCaptain != null) {
            Logger.log("Talking to Collector captain");
            collectorCaptain.interact("Talk-to");
            Sleep.sleepUntil(Dialogues::inDialogue, 5000);
            return 600;
        } else {
            Logger.log("Collector captain not found, checking recruitment scroll board");
            return interactWithRecruitmentBoard();
        }
    }
    
    private int walkToLobby() {
        // Placeholder - would use navigation helper to walk to the lobby
        return 1000;
    }
    
    private int handleDialogue() {
        String[] options = Dialogues.getOptions();
        
        if (options != null) {
            // Look for options related to joining the collector role
            for (int i = 0; i < options.length; i++) {
                if (options[i].contains("Join") || 
                    options[i].contains("I'll be a collector") || 
                    options[i].contains("Yes")) {
                    
                    Dialogues.clickOption(i + 1);
                    Sleep.sleep(600, 1000);
                    roleSelected = true;
                    return 600;
                }
            }
        }
        
        // Continue dialogue
        if (Dialogues.canContinue()) {
            Dialogues.clickContinue();
            return 600;
        }
        
        return 600;
    }
    
    private int interactWithRecruitmentBoard() {
        // Placeholder - would find and interact with the recruitment board
        return 1000;
    }
    
    @Override
    public boolean canExecute() {
        return !roleSelected;
    }
}