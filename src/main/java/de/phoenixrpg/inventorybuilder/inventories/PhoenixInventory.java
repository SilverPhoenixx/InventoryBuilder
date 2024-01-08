package de.phoenixrpg.inventorybuilder.inventories;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class PhoenixInventory implements InventoryHolder {

    protected Inventory inventory;
    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Creates inventory based on it needs
     * @return {@link Inventory} that got created
     */
    public abstract Inventory buildInventory();

    /**
     * The method get called in {@link de.phoenixrpg.inventorybuilder.listener.InventoryClickListener}
     * Runs only when the given Holder is PhoenixInventory
     * @param event is the given event {@link InventoryClickEvent}
     */
    public void runInteract(@NotNull InventoryClickEvent event) {}

    /**
     * The method get called in {@link de.phoenixrpg.inventorybuilder.listener.InventoryCloseListener}
     * Runs only when the given Holder is PhoenixInventory
     * @param event is the given event {@link InventoryClickEvent}
     */
    public void runClose(@NotNull InventoryCloseEvent event) {}

    /**
     * Fills inventory with given itemstack, do nothing when start slot is higher than end slot
     * @param start slot of inventory (not below zero)
     * @param end slot of inventory (not higher than inventory size)
     * @param itemStack that should be set in the given slots
     */
    public void fillItem(int start, int end, @NotNull ItemStack itemStack) {
        start = Math.max(start, 0);
        end = Math.min(end, this.inventory.getSize());

        if(end < start) return;

        for(int slot = start; slot < end; slot++) {
            this.inventory.setItem(slot, itemStack);
        }
    }


}
