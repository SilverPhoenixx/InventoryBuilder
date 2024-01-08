package de.phoenixrpg.inventorybuilder.builder.slot.action;

import com.google.gson.annotations.Expose;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RemoveItemAction extends SlotAction {

    @Expose
    private int minimumAmount;

    @Expose
    private int removeAmount;

    public RemoveItemAction(int minimumAmount, int removeAmount) {
        this.minimumAmount = minimumAmount;
        this.removeAmount = removeAmount;
    }

    @Override
    public void runAction(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        ItemStack itemStack = event.getCurrentItem();

        int currentAmount = itemStack.getAmount();

        if (currentAmount <= minimumAmount) return;

        int newAmount = Math.max(1, currentAmount - removeAmount);

        itemStack.setAmount(newAmount);
    }

    @Override
    public String getName() {
        return "RemoveItem";
    }
}
