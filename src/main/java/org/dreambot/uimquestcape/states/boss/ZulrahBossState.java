/**
 * FILENAME: ZulrahBossState.java
 * Path: src/main/java/org/dreambot/uimquestcape/states/boss/ZulrahBossState.java
 */

package org.dreambot.uimquestcape.states.boss;

import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.uimquestcape.UIMQuestCape;

/**
 * Implementation for fighting Zulrah boss
 */
public class ZulrahBossState extends BossState {
    
    // Constants
    private static final String ZULRAH_NAME = "Zulrah";
    private static final int GREEN_FORM = 2042;
    private static final int BLUE_FORM = 2043;
    private static final int RED_FORM = 2044;
    
    // Rotation tracking
    private int rotationNumber = 0;
    private boolean rotationDetected = false;
    private int phaseCount = 0;
    
    // Safe spots for different rotations
    private final Tile[][] safeSpots = {
        // Rotation 1 safe spots
        {
            new Tile(2213, 3157, 0),
            new Tile(2208, 3157, 0),
            new Tile(2209, 3157, 0),
            // More safe spots for rotation 1...
        },
        // Rotation 2 safe spots
        {
            new Tile(2213, 3157, 0),
            new Tile(2210, 3166, 0),
            new Tile(2209, 3157, 0),
            // More safe spots for rotation 2...
        },
        // Other rotations...
    };
    
    public ZulrahBossState(UIMQuestCape script, String name) {
        super(script, name);
        this.eatAtHealthPercent = 70; // Higher threshold for Zulrah
    }
    
    @Override
    protected void setupForBossFight() {
        // Switch to mage prayer to start
        if (!Prayers.isActive(Prayer.PROTECT_FROM_MAGIC)) {
            Prayers.toggle(Prayer.PROTECT_FROM_MAGIC, true);
        }
        
        // Enable Eagle Eye or Rigour if available
        if (Prayers.getPoints() > 30) {
            if (Prayers.canUse(Prayer.RIGOUR)) {
                Prayers.toggle(Prayer.RIGOUR, true);
            } else if (Prayers.canUse(Prayer.EAGLE_EYE)) {
                Prayers.toggle(Prayer.EAGLE_EYE, true);
            }
        }
    }
    
    @Override
    protected int handlePreparationPhase() {
        // Check if we're at Zulrah's shrine
        Area zulrahArea = new Area(2193, 3184, 2220, 3160);
        if (!zulrahArea.contains(Players.getLocal())) {
            Logger.log("Not at Zulrah area, traveling there");
            // Logic to travel to Zulrah would go here
            return 1000;
        }
        
        // Check if fight has started
        NPC zulrah = NPCs.closest(ZULRAH_NAME);
        if (zulrah != null) {
            Logger.log("Zulrah fight started");
            advancePhase(); // Move to combat phase
            return 600;
        }
        
        // Click on shrine to start fight
        if (interactWithShrine()) {
            Logger.log("Interacting with shrine to start fight");
            Sleep.sleepUntil(() -> NPCs.closest(ZULRAH_NAME) != null, 10000);
            return 1000;
        }
        
        return 600;
    }
    
    @Override
    protected int handleCombatPhase() {
        NPC zulrah = NPCs.closest(ZULRAH_NAME);
        if (zulrah == null) {
            Logger.log("Cannot find Zulrah, checking if defeated");
            return handlePossibleVictory();
        }
        
        // Detect rotation if not detected yet
        if (!rotationDetected) {
            detectRotation(zulrah);
        }
        
        // Handle current phase based on Zulrah's form
        int currentForm = zulrah.getID();
        
        // Move to correct safe spot if rotation detected
        if (rotationDetected) {
            Tile safeSpot = safeSpots[rotationNumber - 1][phaseCount % safeSpots[rotationNumber - 1].length];
            if (Players.getLocal().distance(safeSpot) > 2) {
                Walking.walk(safeSpot);
                return 600;
            }
        }
        
        // Switch gear and prayers based on Zulrah's form
        handleFormSwitch(currentForm);
        
        // Attack Zulrah if not already attacking
        if (!Players.getLocal().isAnimating() && !Players.getLocal().isMoving()) {
            if (zulrah.interact("Attack")) {
                Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), 2000);
            }
        }
        
        // Check for special attacks
        if (isSnakelingPresent() && Players.getLocal().getHealthPercent() < 60) {
            handleSnakelings();
        }
        
        // Check if phase has changed
        checkPhaseChange(zulrah);
        
        // Check for Jad phase
        if (isJadPhase()) {
            advancePhase(); // Move to special attack phase for Jad
            return 600;
        }
        
        return 600;
    }
    
    @Override
    protected int handleSpecialAttackPhase() {
        // This handles the "Jad phase" where Zulrah rapidly switches attack styles
        NPC zulrah = NPCs.closest(ZULRAH_NAME);
        if (zulrah == null) {
            Logger.log("Cannot find Zulrah, checking if defeated");
            return handlePossibleVictory();
        }
        
        // Monitor Zulrah's animation and switch prayers accordingly
        int animation = zulrah.getAnimation();
        
        if (animation == 5069) { // Magic attack animation
            if (!Prayers.isActive(Prayer.PROTECT_FROM_MAGIC)) {
                Prayers.toggle(Prayer.PROTECT_FROM_MAGIC, true);
            }
        } else if (animation == 5072) { // Ranged attack animation
            if (!Prayers.isActive(Prayer.PROTECT_FROM_MISSILES)) {
                Prayers.toggle(Prayer.PROTECT_FROM_MISSILES, true);
            }
        }
        
        // Continue attacking
        if (!Players.getLocal().isAnimating() && !Players.getLocal().isMoving()) {
            if (zulrah.interact("Attack")) {
                Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), 2000);
            }
        }
        
        // Check if Jad phase is over
        if (!isJadPhase()) {
            advancePhase(); // Back to combat phase
        }
        
        return 300; // Faster check rate for prayer switching
    }
    
    private int handlePossibleVictory() {
        // Check if fight is over and we won
        if (NPCs.closest(ZULRAH_NAME) == null && Players.getLocal().getHealthPercent() > 0) {
            Logger.log("Zulrah appears to be defeated");
            Sleep.sleep(2000, 3000); // Wait for loot to appear
            collectLoot();
            advancePhase(); // Move to victory phase
            return 1000;
        }
        return 600;
    }
    
    private void detectRotation(NPC zulrah) {
        // First phase detection
        if (phaseCount == 0) {
            Tile zulrahTile = zulrah.getTile();
            int form = zulrah.getID();
            
            // Determine potential rotation based on first phase position and form
            if (isWestPosition(zulrahTile) && form == GREEN_FORM) {
                Logger.log("Potential Rotation 1 detected");
                rotationNumber = 1;
            } else if (isWestPosition(zulrahTile) && form == BLUE_FORM) {
                Logger.log("Potential Rotation 2 detected");
                rotationNumber = 2;
            } else if (isSouthPosition(zulrahTile) && form == GREEN_FORM) {
                Logger.log("Potential Rotation 3 detected");
                rotationNumber = 3;
            } else if (isSouthPosition(zulrahTile) && form == BLUE_FORM) {
                Logger.log("Potential Rotation 4 detected");
                rotationNumber = 4;
            }
            
            phaseCount++;
        }
        // Second phase confirms rotation
        else if (phaseCount == 1) {
            Tile zulrahTile = zulrah.getTile();
            int form = zulrah.getID();
            
            // Confirm rotation based on second phase
            // This would have more complex logic based on Zulrah's patterns
            
            rotationDetected = true;
            Logger.log("Rotation " + rotationNumber + " confirmed");
        }
    }
    
    private boolean isWestPosition(Tile tile) {
        // Check if Zulrah is at west position
        return tile.getX() < 2210;
    }
    
    private boolean isSouthPosition(Tile tile) {
        // Check if Zulrah is at south position
        return tile.getY() < 3168;
    }
    
    private void handleFormSwitch(int form) {
        switch (form) {
            case GREEN_FORM:
                // Switch to ranged gear
                // Equip blowpipe or crossbow
                if (!Prayers.isActive(Prayer.PROTECT_FROM_RANGED)) {
                    Prayers.toggle(Prayer.PROTECT_FROM_MAGIC, false);
                    Prayers.toggle(Prayer.PROTECT_FROM_RANGED, true);
                }
                break;
            case BLUE_FORM:
                // Switch to magic gear
                // Equip trident
                if (!Prayers.isActive(Prayer.PROTECT_FROM_MAGIC)) {
                    Prayers.toggle(Prayer.PROTECT_FROM_RANGED, false);
                    Prayers.toggle(Prayer.PROTECT_FROM_MAGIC, true);
                }
                break;
            case RED_FORM:
                // Can use either gear set
                // No protection prayer needed
                Prayers.toggle(Prayer.PROTECT_FROM_MAGIC, false);
                Prayers.toggle(Prayer.PROTECT_FROM_RANGED, false);
                break;
        }
    }
    
    private boolean isSnakelingPresent() {
        // Check if dangerous snakelings are present
        return NPCs.closest("Snakeling") != null;
    }
    
    private void handleSnakelings() {
        // Attack closest snakeling
        NPC snakeling = NPCs.closest("Snakeling");
        if (snakeling != null) {
            snakeling.interact("Attack");
            Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), 2000);
        }
    }
    
    private void checkPhaseChange(NPC zulrah) {
        // Track phase changes based on Zulrah diving and reappearing
        if (zulrah.getAnimation() == 5073) { // Diving animation
            Logger.log("Zulrah diving, phase changing");
            phaseCount++;
        }
    }
    
    private boolean isJadPhase() {
        // Determine if current phase is Jad phase
        // This would use rotation and phase count to check
        if (rotationNumber == 1 && phaseCount == 10) return true;
        if (rotationNumber == 2 && phaseCount == 9) return true;
        // Add conditions for other rotations
        return false;
    }
    
    private boolean interactWithShrine() {
        // Interact with the shrine to start fight
        return false; // Placeholder
    }
    
    private void collectLoot() {
        // Logic to collect valuable loot
        Logger.log("Collecting Zulrah loot");
        // Placeholder
    }
}