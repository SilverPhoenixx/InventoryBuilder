package de.phoenixrpg.inventorybuilder.inventories.slot.actions;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.action.CloseInventoryAction;
import de.phoenixrpg.inventorybuilder.inventories.slot.SlotActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.menu.SlotActionMenuInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.ClickAction;
import de.phoenixrpg.inventorybuilder.inventories.consumer.SlotConsumerInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CloseActionInventory extends SlotActionInventory {
    public CloseActionInventory(InteractiveInventory interactiveInventory, ClickAction action) {
        super(interactiveInventory, action);
    }

    @Override
    public Inventory buildInventory() {
        SlotConsumerInventory consumerInventory = new SlotConsumerInventory(getInteractiveInventory(), getClickAction(), "§3Select Slot", event -> {
            save(event.getSlot());
            openInventory((Player) event.getWhoClicked());
        });
        return consumerInventory.buildInventory();
    }
    public void save(int slot) {
        this.getInteractiveInventory().addSlotAction(slot, this.getClickAction(), new CloseInventoryAction());
    }

    public void openInventory(Player player) {
        Inventory slotActionMenuInventory = new SlotActionMenuInventory(this.getInteractiveInventory()).buildInventory();
        player.openInventory(slotActionMenuInventory);
    }
}
