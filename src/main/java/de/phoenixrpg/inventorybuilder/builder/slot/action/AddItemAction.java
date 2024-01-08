package de.phoenixrpg.inventorybuilder.builder.slot.action;

import com.google.gson.annotations.Expose;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AddItemAction extends SlotAction {

    @Expose
    private int maximumAmount;

    @Expose
    private int addAmount;

    public AddItemAction(int maximumAmount, int addAmount) {
        this.maximumAmount = maximumAmount;
        this.addAmount = addAmount;
    }

    @Override
    public void runAction(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        ItemStack itemStack = event.getCurrentItem();

        int currentAmount = itemStack.getAmount();

        if (currentAmount >= maximumAmount) return;

        int newAmount = Math.min(currentAmount + addAmount, itemStack.getMaxStackSize());
        itemStack.setAmount(newAmount);
    }

    @Override
    public String getName() {
        return "AddItem";
    }
}
