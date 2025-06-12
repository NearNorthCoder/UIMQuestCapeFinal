package com.osrsbot.api.modules;

import com.osrsbot.debug.DebugManager;
import java.util.List;

/**
 * Inventory API for querying and interacting with inventory items.
 */
public class InventoryApi {
    public static class Item {
        public final String name;
        public final int id;
        public final int quantity;
        public Item(String name, int id, int quantity) {
            this.name = name; this.id = id; this.quantity = quantity;
        }
    }

    public List<Item> getInventory() {
        DebugManager.logApiCall("InventoryApi.getInventory");
        java.util.ArrayList<Item> result = new java.util.ArrayList<>();
        Object[] items = com.osrsbot.hooks.ClientReflection.getInventoryItems();
        if (items == null) return result;
        try {
            for (Object item : items) {
                java.lang.reflect.Method getId = item.getClass().getMethod("getId");
                java.lang.reflect.Method getQuantity = item.getClass().getMethod("getQuantity");
                java.lang.reflect.Method getName = item.getClass().getMethod("getName"); // May not exist, fallback to id
                int id = ((Number) getId.invoke(item)).intValue();
                int qty = ((Number) getQuantity.invoke(item)).intValue();
                String name = "";
                try {
                    Object n = getName.invoke(item);
                    name = (n != null ? n.toString() : "");
                } catch (Exception e) {
                    // If getName not available, use id as name
                    name = "id_" + id;
                }
                result.add(new Item(name, id, qty));
            }
        } catch (Exception e) {
            DebugManager.logException(e);
        }
        return result;
    }

    public void useItem(String itemName) {
        DebugManager.logApiCall("InventoryApi.useItem(" + itemName + ")");
        // Find the item in inventory and simulate a click
        List<Item> inv = getInventory();
        for (Item item : inv) {
            if (item.name.equalsIgnoreCase(itemName)) {
                // TODO: For demo, click on inventory widget slot (assume inventory is widget 149, slots 0-27)
                int slot = inv.indexOf(item);
                int widgetId = 149 << 16 | slot;
                com.osrsbot.hooks.ClientReflection.clickWidget(widgetId);
                break;
            }
        }
    }

    public void dropItem(String itemName) {
        DebugManager.logApiCall("InventoryApi.dropItem(" + itemName + ")");
        // Find the item and simulate right-click drop action (just click for demo)
        useItem(itemName); // In a real implementation, right-click and select "Drop"
    }
}