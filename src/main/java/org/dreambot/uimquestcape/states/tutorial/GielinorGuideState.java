package org.dreambot.uimquestcape.states.tutorial;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.QuestVarbitManager;

public class GielinorGuideState extends AbstractState {
    private static final int TUTORIAL_PROGRESS_VARBIT = 281;
    private static final int GIELINOR_GUIDE_AREA_PROGRESS = 3;
    private static final Area GIELINOR_GUIDE_AREA = new Area(
            new Tile(3090, 3107, 0),
            new Tile(3097, 3100, 0)
    );

    private boolean talkedToGuide = false;
    private boolean selectedUIM = false;
    private boolean openedSettings = false;

    public GielinorGuideState(UIMQuestCape script) {
        super(script, "GielinorGuideState");
    }

    @Override
    public int execute() {
        int progress = QuestVarbitManager.getVarbit(TUTORIAL_PROGRESS_VARBIT);

        // If we've progressed past this stage, mark as complete
        if (progress > GIELINOR_GUIDE_AREA_PROGRESS) {
            complete();
            return 600;
        }

        // If we're not at Gielinor Guide area, walk there
        if (!GIELINOR_GUIDE_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to Gielinor Guide");
            Walking.walk(GIELINOR_GUIDE_AREA.getRandomTile());
            return 1000;
        }

        // Handle dialogues if active
        if (Dialogues.inDialogue()) {
            return handleDialogue();
        }

        // Open settings panel if prompted
        if (!openedSettings && progress == 3) {
            Logger.log("Opening settings tab");
            WidgetChild settingsTab = Widgets.getWidgetChild(164, 41);
            if (settingsTab != null && settingsTab.isVisible()) {
                settingsTab.interact();
                Sleep.sleepUntil(() -> PlayerSettings.getBitValue(TUTORIAL_PROGRESS_VARBIT) > 3, 3000);
                openedSettings = true;
                return 600;
            }
        }

        // Talk to Gielinor Guide if we haven't
        if (!talkedToGuide) {
            Logger.log("Talking to Gielinor Guide");
            NPC guide = NPCs.closest("Gielinor Guide");
            if (guide != null) {
                guide.interact("Talk-to");
                Sleep.sleepUntil(Dialogues::inDialogue, 5000);
                return 600;
            }
        }

        return 600;
    }

    private int handleDialogue() {
        // Get dialogue text instead of NPC name
        String dialogueText = Dialogues.getNPCDialogue();

        // Handle Paul dialogue for Ironman mode selection
        if (isPaulDialogue(dialogueText) && !selectedUIM) {
            String[] options = Dialogues.getOptions();

            // Select Ultimate Ironman mode
            if (options != null) {
                for (int i = 0; i < options.length; i++) {
                    if (options[i].contains("Ultimate")) {
                        Dialogues.clickOption(i + 1);
                        Sleep.sleep(600, 1000);
                        selectedUIM = true;
                        return 600;
                    }
                }
            }
        }

        // Continue dialogue with Gielinor Guide
        if (isGielinorGuideDialogue(dialogueText)) {
            talkedToGuide = true;
        }

        // Continue dialogue
        if (Dialogues.canContinue()) {
            Dialogues.clickContinue();
            return 600;
        } else if (Dialogues.getOptions() != null) {
            // For general option selection that's not Paul dialogue
            Dialogues.clickOption(1);
            return 600;
        }

        return 600;
    }

    // Helper method to check if this is Paul's dialogue
    private boolean isPaulDialogue(String dialogueText) {
        return dialogueText != null &&
                (dialogueText.contains("Paul") ||
                        dialogueText.contains("ironman") ||
                        dialogueText.contains("Ironman"));
    }

    // Helper method to check if this is Gielinor Guide's dialogue
    private boolean isGielinorGuideDialogue(String dialogueText) {
        return dialogueText != null &&
                (dialogueText.contains("Gielinor Guide") ||
                        dialogueText.contains("tutorial island") ||
                        dialogueText.contains("Tutorial Island"));
    }

    @Override
    public boolean canExecute() {
        int progress = QuestVarbitManager.getVarbit(TUTORIAL_PROGRESS_VARBIT);
        return progress >= 0 && progress <= GIELINOR_GUIDE_AREA_PROGRESS;
    }
}