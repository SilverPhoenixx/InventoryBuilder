package de.phoenixrpg.inventorybuilder.listener;

import de.phoenixrpg.inventorybuilder.inventories.PhoenixInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    /**
     * The listener is handling every inventory click on the server
     * Checks if the inventory holder is our custom holder {@link PhoenixInventory}
     * Checks if the click is NOT the player inventory
     *
     * Runs the implemented {@code runInteract} method from {@link PhoenixInventory}
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getInventory().getHolder() instanceof PhoenixInventory interactiveInventory)) return;
        if(event.getRawSlot() < 0) return;
        if(event.getRawSlot() >= event.getView().getTopInventory().getSize()) return;

        event.setCancelled(true);

        interactiveInventory.runInteract(event);
    }
}
