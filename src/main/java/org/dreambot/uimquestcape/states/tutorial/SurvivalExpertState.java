package org.dreambot.uimquestcape.states.tutorial;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.QuestVarbitManager;

public class SurvivalExpertState extends AbstractState {
    private static final int TUTORIAL_PROGRESS_VARBIT = 281;
    private static final int SURVIVAL_EXPERT_START = 10;
    private static final int SURVIVAL_EXPERT_END = 20;
    
    private static final Area SURVIVAL_EXPERT_AREA = new Area(
            new Tile(3100, 3095, 0),
            new Tile(3108, 3089, 0)
    );
    
    private boolean talkedToExpert = false;
    private boolean cutTree = false;
    private boolean madeFireFromLogs = false;
    private boolean caughtShrimp = false;
    private boolean cookedShrimp = false;
    private boolean doneGatePart = false;
    
    public SurvivalExpertState(UIMQuestCape script) {
        super(script, "SurvivalExpertState");
    }
    
    @Override
    public int execute() {
        int progress = QuestVarbitManager.getVarbit(TUTORIAL_PROGRESS_VARBIT);
        
        // If we've progressed past this stage, mark as complete
        if (progress >= SURVIVAL_EXPERT_END) {
            complete();
            return 600;
        }
        
        // Handle dialogues if active
        if (Dialogues.inDialogue()) {
            return handleDialogue();
        }
        
        // If we're done with this section, proceed through gate
        if (progress >= 20 && !doneGatePart) {
            return handleGate();
        }
        
        // If we've cooked shrimp, talk to survival expert again
        if (cookedShrimp && !doneGatePart) {
            return talkToSurvivalExpert();
        }
        
        // If we've caught shrimp but not cooked it
        if (caughtShrimp && !cookedShrimp) {
            return cookShrimp();
        }
        
        // If we've made fire but not caught shrimp
        if (madeFireFromLogs && !caughtShrimp) {
            return catchShrimp();
        }
        
        // If we've cut a tree but not made a fire
        if (cutTree && !madeFireFromLogs) {
            return makeFire();
        }
        
        // If we've talked to expert but not cut a tree
        if (talkedToExpert && !cutTree) {
            return cutTree();
        }
        
        // If we're not at Survival Expert area, walk there
        if (!SURVIVAL_EXPERT_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to Survival Expert");
            Walking.walk(SURVIVAL_EXPERT_AREA.getRandomTile());
            return 1000;
        }
        
        // Talk to Survival Expert if we haven't
        return talkToSurvivalExpert();
    }
    
    private int handleDialogue() {
        String npcName = Dialogues.getNPCName();
        
        // Mark that we've talked to the survival expert
        if ("Survival Expert".equals(npcName)) {
            talkedToExpert = true;
        }
        
        // Continue dialogue
        if (Dialogues.canContinue()) {
            Dialogues.clickContinue();
            return 600;
        } else if (Dialogues.getOptions() != null) {
            Dialogues.clickOption(1);
            return 600;
        }
        
        return 600;
    }
    
    private int talkToSurvivalExpert() {
        NPC survivalExpert = NPCs.closest("Survival Expert");
        if (survivalExpert != null) {
            Logger.log("Talking to Survival Expert");
            survivalExpert.interact("Talk-to");
            Sleep.sleepUntil(Dialogues::inDialogue, 5000);
        }
        return 600;
    }
    
    private int cutTree() {
        // Check if we already have logs
        if (Inventory.contains("Logs")) {
            cutTree = true;
            return 600;
        }
        
        // Check if we have an axe
        if (!Inventory.contains("Bronze axe")) {
            // Look around for axe
            GameObject axeStump = GameObjects.closest("Survival guide");
            if (axeStump != null) {
                axeStump.interact("Take-axe");
                Sleep.sleepUntil(() -> Inventory.contains("Bronze axe"), 5000);
            }
            return 600;
        }
        
        // Find a tree to cut
        GameObject tree = GameObjects.closest(obj -> 
            obj != null && 
            obj.getName() != null && 
            obj.getName().equals("Tree") && 
            obj.hasAction("Chop down")
        );
        
        if (tree != null) {
            Logger.log("Chopping tree");
            tree.interact("Chop down");
            Sleep.sleepUntil(() -> Players.getLocal().isAnimating() || Inventory.contains("Logs"), 5000);
            Sleep.sleepUntil(() -> !Players.getLocal().isAnimating() || Inventory.contains("Logs"), 10000);
            
            if (Inventory.contains("Logs")) {
                cutTree = true;
            }
        }
        
        return 600;
    }
    
    private int makeFire() {
        // Check if fire exists already
        GameObject fire = GameObjects.closest("Fire");
        if (fire != null && fire.distance() < 10) {
            Logger.log("Fire already exists");
            madeFireFromLogs = true;
            return 600;
        }
        
        // Check if we have logs and tinderbox
        if (!Inventory.contains("Logs")) {
            Logger.log("No logs, need to cut a tree");
            cutTree = false;
            return cutTree();
        }
        
        if (!Inventory.contains("Tinderbox")) {
            // Find tinderbox in the area
            GameObject tinderboxSpawn = GameObjects.closest("Survival guide");
            if (tinderboxSpawn != null) {
                tinderboxSpawn.interact("Take-tinderbox");
                Sleep.sleepUntil(() -> Inventory.contains("Tinderbox"), 5000);
            }
            return 600;
        }
        
        // Use tinderbox on logs
        Logger.log("Making fire");
        Item tinderbox = Inventory.get("Tinderbox");
        Item logs = Inventory.get("Logs");
        
        if (tinderbox != null && logs != null) {
            tinderbox.useOn(logs);
            Sleep.sleepUntil(() -> {
                GameObject newFire = GameObjects.closest("Fire");
                return newFire != null && newFire.distance() < 10;
            }, 5000);
            
            GameObject newFire = GameObjects.closest("Fire");
            if (newFire != null && newFire.distance() < 10) {
                madeFireFromLogs = true;
            }
        }
        
        return 600;
    }
    
    private int catchShrimp() {
        // Check if we already have shrimp/fish
        if (Inventory.contains("Raw shrimps")) {
            caughtShrimp = true;
            return 600;
        }
        
        // Find fishing spot
        NPC fishingSpot = NPCs.closest(npc -> 
            npc != null && 
            npc.getName() != null && 
            npc.getName().contains("Fishing spot") && 
            npc.hasAction("Net")
        );
        
        if (fishingSpot != null) {
            Logger.log("Fishing for shrimp");
            fishingSpot.interact("Net");
            Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), 5000);
            Sleep.sleepUntil(() -> !Players.getLocal().isAnimating() || Inventory.contains("Raw shrimps"), 30000);
            
            if (Inventory.contains("Raw shrimps")) {
                caughtShrimp = true;
            }
        } else {
            // If no fishing spot found, walk to water area
            Tile waterTile = new Tile(3101, 3092, 0);
            Walking.walk(waterTile);
        }
        
        return 600;
    }
    
    private int cookShrimp() {
        // Check if we already have cooked shrimp
        if (Inventory.contains("Shrimps")) {
            cookedShrimp = true;
            return 600;
        }
        
        // Check if we have raw shrimp
        if (!Inventory.contains("Raw shrimps")) {
            Logger.log("No raw shrimp, need to fish");
            caughtShrimp = false;
            return catchShrimp();
        }
        
        // Find a fire to cook on
        GameObject fire = GameObjects.closest("Fire");
        if (fire == null || fire.distance() > 10) {
            Logger.log("No fire nearby, need to make one");
            madeFireFromLogs = false;
            cutTree = true;  // We already cut a tree before, so skip that step
            return makeFire();
        }
        
        // Cook the shrimp
        Logger.log("Cooking shrimp");
        Item rawShrimps = Inventory.get("Raw shrimps");
        if (rawShrimps != null) {
            rawShrimps.useOn(fire);
            Sleep.sleepUntil(() -> Inventory.contains("Shrimps") || !Inventory.contains("Raw shrimps"), 5000);
            
            if (Inventory.contains("Shrimps")) {
                cookedShrimp = true;
            }
        }
        
        return 600;
    }
    
    private int handleGate() {
        // Find gate to proceed
        GameObject gate = GameObjects.closest(obj -> 
            obj != null && 
            obj.getName() != null && 
            obj.getName().contains("Gate") && 
            obj.distance() < 15 && 
            obj.hasAction("Open")
        );
        
        if (gate != null) {
            Logger.log("Opening gate to proceed");
            gate.interact("Open");
            Sleep.sleepUntil(() -> 
                QuestVarbitManager.getVarbit(TUTORIAL_PROGRESS_VARBIT) >= SURVIVAL_EXPERT_END, 
                5000
            );
            doneGatePart = true;
        } else {
            // Walk toward gate area
            Tile gateTile = new Tile(3090, 3092, 0);
            Walking.walk(gateTile);
        }
        
        return 600;
    }
    
    @Override
    public boolean canExecute() {
        int progress = QuestVarbitManager.getVarbit(TUTORIAL_PROGRESS_VARBIT);
        return progress >= SURVIVAL_EXPERT_START && progress < SURVIVAL_EXPERT_END;
    }
}