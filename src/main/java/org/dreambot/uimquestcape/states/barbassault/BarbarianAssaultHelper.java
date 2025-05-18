package org.dreambot.uimquestcape.states.barbassault;

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
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

/**
 * Helper class for Barbarian Assault minigame states
 */
public class BarbarianAssaultHelper {

    // Area definitions
    public static final Area BARBARIAN_ASSAULT_AREA = new Area(2520, 3573, 2565, 3538);
    public static final Area LOBBY_AREA = new Area(2530, 3577, 2540, 3566);

    // Room areas
    public static final Area ATTACKER_ROOM = new Area(2570, 5130, 2590, 5110);
    public static final Area DEFENDER_ROOM = new Area(2540, 5130, 2560, 5110);
    public static final Area COLLECTOR_ROOM = new Area(2560, 5130, 2580, 5110);
    public static final Area HEALER_ROOM = new Area(2560, 5110, 2580, 5090);

    // Widget IDs
    public static final int ATTACKER_INTERFACE_GROUP = 485;
    public static final int DEFENDER_INTERFACE_GROUP = 486;
    public static final int COLLECTOR_INTERFACE_GROUP = 484;
    public static final int HEALER_INTERFACE_GROUP = 487;

    public static final int HORN_CALL_CHILD = 4;
    public static final int POINTS_CHILD = 7;

    // Game over/wave complete widgets
    public static final int GAME_OVER_GROUP = 312;
    public static final int WAVE_COMPLETE_GROUP = 311;

    /**
     * Check if in the main Barbarian Assault area
     */
    public static boolean isAtBarbarianAssault() {
        return BARBARIAN_ASSAULT_AREA.contains(Players.getLocal());
    }

    /**
     * Check if in the Barbarian Assault lobby
     */
    public static boolean isInLobby() {
        return LOBBY_AREA.contains(Players.getLocal());
    }

    /**
     * Check if in a specific role room
     */
    public static boolean isInRoleRoom(String role) {
        switch (role.toLowerCase()) {
            case "attacker":
                return ATTACKER_ROOM.contains(Players.getLocal());
            case "defender":
                return DEFENDER_ROOM.contains(Players.getLocal());
            case "collector":
                return COLLECTOR_ROOM.contains(Players.getLocal());
            case "healer":
                return HEALER_ROOM.contains(Players.getLocal());
            default:
                return false;
        }
    }

    /**
     * Get the call (what to do) for a specific role
     */
    public static String getCallForRole(String role) {
        int interfaceGroup;

        switch (role.toLowerCase()) {
            case "attacker":
                interfaceGroup = ATTACKER_INTERFACE_GROUP;
                break;
            case "defender":
                interfaceGroup = DEFENDER_INTERFACE_GROUP;
                break;
            case "collector":
                interfaceGroup = COLLECTOR_INTERFACE_GROUP;
                break;
            case "healer":
                interfaceGroup = HEALER_INTERFACE_GROUP;
                break;
            default:
                return "";
        }

        WidgetChild callWidget = Widgets.getWidgetChild(interfaceGroup, HORN_CALL_CHILD);
        if (callWidget != null && callWidget.isVisible()) {
            return callWidget.getText();
        }

        return "";
    }

    /**
     * Get points for a specific role
     */
    public static int getPointsForRole(String role) {
        int interfaceGroup;

        switch (role.toLowerCase()) {
            case "attacker":
                interfaceGroup = ATTACKER_INTERFACE_GROUP;
                break;
            case "defender":
                interfaceGroup = DEFENDER_INTERFACE_GROUP;
                break;
            case "collector":
                interfaceGroup = COLLECTOR_INTERFACE_GROUP;
                break;
            case "healer":
                interfaceGroup = HEALER_INTERFACE_GROUP;
                break;
            default:
                return 0;
        }

        WidgetChild pointsWidget = Widgets.getWidgetChild(interfaceGroup, POINTS_CHILD);
        if (pointsWidget != null && pointsWidget.isVisible()) {
            try {
                return Integer.parseInt(pointsWidget.getText());
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    /**
     * Check if the game over interface is visible
     */
    public static boolean isGameOver() {
        WidgetChild gameOverWidget = Widgets.getWidgetChild(GAME_OVER_GROUP, 0);
        return gameOverWidget != null && gameOverWidget.isVisible();
    }

    /**
     * Close the game over interface
     */
    public static void closeGameOverInterface() {
        WidgetChild closeButton = Widgets.getWidgetChild(GAME_OVER_GROUP, 19);
        if (closeButton != null && closeButton.isVisible()) {
            closeButton.interact();
            Sleep.sleep(1000, 2000);
        }
    }

    /**
     * Check if the wave completed interface is visible
     */
    public static boolean isWaveCompleted() {
        WidgetChild waveCompletedWidget = Widgets.getWidgetChild(WAVE_COMPLETE_GROUP, 0);
        return waveCompletedWidget != null && waveCompletedWidget.isVisible();
    }

    /**
     * Enter a specific role room
     */
    public static boolean enterRoleRoom(String role) {
        if (!isInLobby()) {
            return false;
        }

        String roomName = role + " room";
        GameObject entrance = GameObjects.closest(obj ->
                obj != null &&
                        obj.getName() != null &&
                        obj.getName().equals(roomName) &&
                        obj.hasAction("Enter"));

        if (entrance != null) {
            Logger.log("Entering " + role + " room");
            entrance.interact("Enter");

            Sleep.sleepUntil(() -> {
                switch (role.toLowerCase()) {
                    case "attacker":
                        return ATTACKER_ROOM.contains(Players.getLocal());
                    case "defender":
                        return DEFENDER_ROOM.contains(Players.getLocal());
                    case "collector":
                        return COLLECTOR_ROOM.contains(Players.getLocal());
                    case "healer":
                        return HEALER_ROOM.contains(Players.getLocal());
                    default:
                        return false;
                }
            }, 5000);

            return true;
        }

        return false;
    }

    /**
     * Find and interact with role captain
     */
    public static boolean talkToRoleCaptain(String role) {
        if (!isInLobby()) {
            return false;
        }

        NPC captain = NPCs.closest(npc ->
                npc != null &&
                        npc.getName() != null &&
                        npc.getName().contains("Captain") &&
                        npc.hasAction("Talk-to") &&
                        npc.getOverheadText() != null &&
                        npc.getOverheadText().contains(role)
        );

        if (captain != null) {
            Logger.log("Talking to " + role + " captain");
            captain.interact("Talk-to");
            return Sleep.sleepUntil(Dialogues::inDialogue, 5000);
        }

        return false;
    }

    /**
     * Handle dialogues for joining a role
     */
    public static boolean handleRoleJoinDialogue() {
        if (!Dialogues.inDialogue()) {
            return false;
        }

        String[] options = Dialogues.getOptions();

        if (options != null) {
            for (int i = 0; i < options.length; i++) {
                if (options[i].contains("Join") ||
                        options[i].contains("I'll be") ||
                        options[i].contains("Yes")) {

                    Dialogues.clickOption(i + 1);
                    Sleep.sleep(600, 1000);
                    return true;
                }
            }
        }

        if (Dialogues.canContinue()) {
            Dialogues.clickContinue();
        }

        return false;
    }

    /**
     * Check if enough teammates have joined
     */
    public static boolean hasTeamFormedForWave() {
        // This is a simple placeholder. In a real implementation,
        // we'd check specific widgets that indicate team readiness
        return true;
    }
}