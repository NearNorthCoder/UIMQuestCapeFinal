package org.dreambot.uimquestcape.states.earlyessentials;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.widgets.WidgetChild;
import org.dreambot.uimquestcape.AbstractState;
import org.dreambot.uimquestcape.UIMQuestCape;
import org.dreambot.uimquestcape.util.NavigationHelper;
import org.dreambot.uimquestcape.util.QuestVarbitManager;

public class EnterStrongholdState extends AbstractState {
    
    private static final Area BARBARIAN_VILLAGE_AREA = new Area(
            new Tile(3072, 3433, 0),
            new Tile(3089, 3415, 0)
    );
    
    private static final Area STRONGHOLD_ENTRANCE_AREA = new Area(
            new Tile(3078, 3425, 0),
            new Tile(3084, 3420, 0)
    );
    
    private static final int STRONGHOLD_VARBIT = 853;
    private static final int STRONGHOLD_STARTED = 1;
    
    private final NavigationHelper navigation;
    private boolean securityQuestionsAnswered = false;
    
    public EnterStrongholdState(UIMQuestCape script) {
        super(script, "EnterStrongholdState");
        this.navigation = new NavigationHelper(script);
    }
    
    @Override
    public int execute() {
        // Check if we've made progress on the stronghold
        if (QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) >= STRONGHOLD_STARTED) {
            Logger.log("Entered stronghold successfully");
            complete();
            return 600;
        }
        
        // Handle security questions if they're open
        if (isSecurityQuestionOpen()) {
            return handleSecurityQuestion();
        }
        
        // If we're in dialogue, handle it
        if (Dialogues.inDialogue()) {
            Dialogues.clickContinue();
            return 600;
        }
        
        // If we're not at Barbarian Village, walk there
        if (!BARBARIAN_VILLAGE_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to Barbarian Village");
            Walking.walkExact(BARBARIAN_VILLAGE_AREA.getCenter());
            return 1000;
        }
        
        // If we're not at the Stronghold entrance, walk there
        if (!STRONGHOLD_ENTRANCE_AREA.contains(Players.getLocal())) {
            Logger.log("Walking to Stronghold entrance");
            Walking.walk(STRONGHOLD_ENTRANCE_AREA.getCenter());
            return 600;
        }
        
        // Enter the stronghold
        GameObject strongholdEntrance = GameObjects.closest(obj -> 
            obj != null && 
            obj.getName() != null && 
            obj.getName().equals("Entrance") && 
            obj.hasAction("Climb-down") && 
            STRONGHOLD_ENTRANCE_AREA.contains(obj)
        );
        
        if (strongholdEntrance != null) {
            Logger.log("Entering Stronghold of Security");
            strongholdEntrance.interact("Climb-down");
            
            // Wait for dialogue or the security question
            Sleep.sleepUntil(() -> 
                Dialogues.inDialogue() || 
                isSecurityQuestionOpen() || 
                QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) >= STRONGHOLD_STARTED, 
                5000
            );
        }
        
        return 600;
    }
    
    private boolean isSecurityQuestionOpen() {
        // Check if the security question interface is open
        WidgetChild securityQuestion = Widgets.getWidgetChild(213, 0);
        return securityQuestion != null && securityQuestion.isVisible();
    }
    
    private int handleSecurityQuestion() {
        Logger.log("Handling security question");
        
        // Get the security question widget
        WidgetChild securityQuestion = Widgets.getWidgetChild(213, 2);
        if (securityQuestion == null || !securityQuestion.isVisible()) {
            return 600;
        }
        
        // Check for answer options
        WidgetChild[] options = new WidgetChild[4];
        options[0] = Widgets.getWidgetChild(213, 6);
        options[1] = Widgets.getWidgetChild(213, 7);
        options[2] = Widgets.getWidgetChild(213, 8);
        options[3] = Widgets.getWidgetChild(213, 9);
        
        // Get the question text
        String questionText = securityQuestion.getText();
        if (questionText == null) {
            return 600;
        }
        
        // Find the correct answer
        int correctAnswer = determineCorrectAnswer(questionText, options);
        if (correctAnswer >= 0 && correctAnswer < options.length) {
            WidgetChild correctOption = options[correctAnswer];
            if (correctOption != null && correctOption.isVisible()) {
                Logger.log("Selecting answer: " + correctOption.getText());
                correctOption.interact();
                Sleep.sleep(1000, 2000);
                securityQuestionsAnswered = true;
                
                // Check if we've made progress after answering
                return 600;
            }
        }
        
        // If we couldn't find a correct answer, try first option
        if (options[0] != null && options[0].isVisible()) {
            options[0].interact();
            Sleep.sleep(1000, 2000);
        }
        
        return 600;
    }
    
    private int determineCorrectAnswer(String question, WidgetChild[] options) {
        // Simple logic to determine the correct answer
        // In a real implementation, this would have rules for all security questions
        
        question = question.toLowerCase();
        
        // Check common security questions and their correct answers
        if (question.contains("password") && question.contains("give")) {
            // Question about giving password
            for (int i = 0; i < options.length; i++) {
                if (options[i] != null && options[i].getText() != null && 
                    (options[i].getText().contains("Never") || 
                     options[i].getText().contains("not"))) {
                    return i;
                }
            }
        } else if (question.contains("jagex") && question.contains("email")) {
            // Question about Jagex sending emails
            for (int i = 0; i < options.length; i++) {
                if (options[i] != null && options[i].getText() != null && 
                    options[i].getText().contains("Legit")) {
                    return i;
                }
            }
        } else if (question.contains("authenticator")) {
            // Question about authenticator
            for (int i = 0; i < options.length; i++) {
                if (options[i] != null && options[i].getText() != null && 
                    options[i].getText().contains("secure")) {
                    return i;
                }
            }
        } else if (question.contains("bank pin")) {
            // Question about bank pin
            for (int i = 0; i < options.length; i++) {
                if (options[i] != null && options[i].getText() != null && 
                    options[i].getText().contains("secure")) {
                    return i;
                }
            }
        }
        
        // Default to option 0 if we can't determine the answer
        return 0;
    }
    
    @Override
    public boolean canExecute() {
        return QuestVarbitManager.getVarbit(STRONGHOLD_VARBIT) < STRONGHOLD_STARTED;
    }
}