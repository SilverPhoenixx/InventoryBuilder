package de.phoenixrpg.inventorybuilder.builder.slot.action;

import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class SlotAction {

    public abstract void runAction(InventoryClickEvent event);
    public abstract String getName();
}
