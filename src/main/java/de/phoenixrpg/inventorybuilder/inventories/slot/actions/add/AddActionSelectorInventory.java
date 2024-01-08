package de.phoenixrpg.inventorybuilder.inventories.slot.actions.add;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.inventories.slot.SlotActionInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.ClickAction;
import de.phoenixrpg.inventorybuilder.inventories.consumer.SlotConsumerInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class AddActionSelectorInventory extends SlotActionInventory {
    public AddActionSelectorInventory(InteractiveInventory interactiveInventory, ClickAction clickAction) {
        super(interactiveInventory, clickAction);
    }

    @Override
    public Inventory buildInventory() {
        SlotConsumerInventory slotConsumerInventory = new SlotConsumerInventory(this.getInteractiveInventory(), this.getClickAction(), "§3Select Slot",
                event -> {
                    Player player = (Player) event.getWhoClicked();

                    Inventory addActionInventory = new AddActionInventory(this.getInteractiveInventory(), this.getClickAction(), event.getSlot()).buildInventory();
                    player.openInventory(addActionInventory);

                });

        return slotConsumerInventory.buildInventory();
    }
}
