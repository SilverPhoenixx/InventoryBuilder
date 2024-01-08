package de.phoenixrpg.inventorybuilder.listener;

import de.phoenixrpg.inventorybuilder.inventories.PhoenixInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    /**
     * The listener is handling every inventory close on the server
     * Checks if the inventory holder is our custom holder {@link PhoenixInventory}
     *
     * Runs the implemented {@code runClose} method from {@link PhoenixInventory}
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!(event.getInventory().getHolder() instanceof PhoenixInventory interactiveInventory)) return;
        interactiveInventory.runClose(event);
    }
}
