package com.osrsbot.api.modules;

import com.osrsbot.debug.DebugManager;

/**
 * Widget API for reading and interacting with RuneLite UI widgets.
 */
public class WidgetApi {
    public boolean isWidgetVisible(int widgetId) {
        DebugManager.logApiCall("WidgetApi.isWidgetVisible(" + widgetId + ")");
        return com.osrsbot.hooks.ClientReflection.isWidgetVisible(widgetId);
    }

    public void clickWidget(int widgetId) {
        DebugManager.logApiCall("WidgetApi.clickWidget(" + widgetId + ")");
        com.osrsbot.hooks.ClientReflection.clickWidget(widgetId);
    }
}