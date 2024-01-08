package de.phoenixrpg.inventorybuilder.builder.items.action;

import com.google.gson.annotations.Expose;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SetItemAction extends ItemAction {

    @Expose
    private int slot;

    public SetItemAction(ItemStack itemStack, int slot) {
        super(itemStack);
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public void runAction(Inventory inventory) {
        inventory.setItem(slot, getItemStack().clone());
    }

}
