package de.phoenixrpg.inventorybuilder.builder.items.action;

import com.google.gson.annotations.Expose;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class ItemAction {

    @Expose
    private ItemStack itemStack;

    public ItemAction(ItemStack itemStack) {
        this.itemStack = itemStack;
    }


    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public abstract void runAction(Inventory inventory);
}
