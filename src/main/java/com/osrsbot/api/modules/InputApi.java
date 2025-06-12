package com.osrsbot.api.modules;

import com.osrsbot.debug.DebugManager;
import java.awt.*;

/**
 * Input API for simulating mouse/keyboard input.
 */
public class InputApi {
    private static java.awt.Robot robot = createRobot();

    private static java.awt.Robot createRobot() {
        try {
            return new java.awt.Robot();
        } catch (Exception e) {
            DebugManager.logException(e);
            return null;
        }
    }

    public void click(int x, int y) {
        DebugManager.logApiCall("InputApi.click(" + x + ", " + y + ")");
        moveMouse(x, y);
        if (robot != null) {
            robot.mousePress(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(30);
            robot.mouseRelease(java.awt.event.InputEvent.BUTTON1_DOWN_MASK);
        }
    }

    public void rightClick(int x, int y) {
        DebugManager.logApiCall("InputApi.rightClick(" + x + ", " + y + ")");
        moveMouse(x, y);
        if (robot != null) {
            robot.mousePress(java.awt.event.InputEvent.BUTTON3_DOWN_MASK);
            robot.delay(30);
            robot.mouseRelease(java.awt.event.InputEvent.BUTTON3_DOWN_MASK);
        }
    }

    public void moveMouse(int x, int y) {
        DebugManager.logApiCall("InputApi.moveMouse(" + x + ", " + y + ")");
        if (robot != null) {
            robot.mouseMove(x, y);
        }
    }

    public void type(String text) {
        DebugManager.logApiCall("InputApi.type(" + text + ")");
        if (robot != null) {
            for (char c : text.toCharArray()) {
                try {
                    boolean upper = Character.isUpperCase(c);
                    int keyCode = java.awt.event.KeyEvent.getExtendedKeyCodeForChar(c);
                    if (upper) {
                        robot.keyPress(java.awt.event.KeyEvent.VK_SHIFT);
                    }
                    robot.keyPress(keyCode);
                    robot.keyRelease(keyCode);
                    if (upper) {
                        robot.keyRelease(java.awt.event.KeyEvent.VK_SHIFT);
                    }
                    robot.delay(10);
                } catch (Exception e) {
                    DebugManager.logException(e);
                }
            }
        }
    }
}