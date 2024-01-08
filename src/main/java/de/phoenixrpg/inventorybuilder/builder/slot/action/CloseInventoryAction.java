package de.phoenixrpg.inventorybuilder.builder.slot.action;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CloseInventoryAction extends SlotAction {

    @Override
    public void runAction(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        player.closeInventory();
    }

    @Override
    public String getName() {
        return "CloseInventory";
    }

}
