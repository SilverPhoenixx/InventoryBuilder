package de.phoenixrpg.inventorybuilder.inventories.consumer;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.builder.items.action.ItemAction;
import de.phoenixrpg.inventorybuilder.builder.items.action.SetItemAction;
import de.phoenixrpg.inventorybuilder.adapter.PhoenixCraftInventory;
import de.phoenixrpg.inventorybuilder.inventories.slot.SlotActionInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * The inventory is created from the ItemActions of the given {@link InteractiveInventory}
 *
 * The consumer can be use to detect the clicked slot and working with that
 *
 * Is a good for SlotAction because you first need the slot number, where the player wants to put the action
 */
public class SlotConsumerInventory extends SlotActionInventory {

    private final Consumer<InventoryClickEvent> consumer;

    private final String title;

    public SlotConsumerInventory(InteractiveInventory interactiveInventory, ClickAction action, String title, Consumer<InventoryClickEvent> consumer) {
        super(interactiveInventory, action);

        this.title = title;
        this.consumer = consumer;
    }

    /**
     * The inventory is created from the ItemActions of the given {@link InteractiveInventory}
     * @return {@code Inventory}
     */
    @Override
    public Inventory buildInventory() {
        if (this.getInteractiveInventory().getInventoryType() == InventoryType.CHEST) {
            this.inventory = Bukkit.createInventory(this, this.getInteractiveInventory().getSize(), this.title);
        } else {
            this.inventory = new PhoenixCraftInventory(this, this.getInteractiveInventory().getInventoryType(), this.title);
        }

        this.getInteractiveInventory().sortItemActions();

        for (ItemAction itemAction : this.getInteractiveInventory().getItemActions()) {
            if (!(itemAction instanceof SetItemAction)) continue;
            itemAction.runAction(this.inventory);
        }

        return this.inventory;
    }

    @Override
    public void runInteract(@NotNull InventoryClickEvent event) {
        consumer.accept(event);
    }
}
