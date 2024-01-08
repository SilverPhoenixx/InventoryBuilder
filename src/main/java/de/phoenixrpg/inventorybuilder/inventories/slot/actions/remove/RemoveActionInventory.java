package de.phoenixrpg.inventorybuilder.inventories.slot.actions.remove;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import de.phoenixrpg.inventorybuilder.builder.slot.action.RemoveItemAction;
import de.phoenixrpg.inventorybuilder.inventories.menu.SlotActionMenuInventory;
import de.phoenixrpg.inventorybuilder.inventories.slot.SlotActionInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class RemoveActionInventory extends SlotActionInventory {


    private final int slot;

    private int removeAmount;
    private int minimumAmount;

    public RemoveActionInventory(InteractiveInventory interactiveInventory, ClickAction clickAction, int slot) {
        super(interactiveInventory, clickAction);

        this.slot = slot;

        this.removeAmount = 1;
        this.minimumAmount = 1;
    }

    @Override
    public Inventory buildInventory() {
        this.inventory = Bukkit.createInventory(this, 9, "§3Remove Item Action");

        this.fillItem(0, 9, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());

        this.inventory.setItem(0, new ItemBuilder(Material.LIGHT_GRAY_SHULKER_BOX).setName("§7Back to menu").build());

        this.setRemoveAmount();
        this.setMinimumAmount();

        this.inventory.setItem(8, new ItemBuilder(Material.LIME_SHULKER_BOX).setName("§aSave changes").build());

        return this.inventory;
    }

    @Override
    public void runInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        int amount = 0;

        switch (event.getClick()) {
            case RIGHT -> amount = 1;
            case SHIFT_RIGHT -> amount = 10;
            case LEFT -> amount = -1;
            case SHIFT_LEFT -> amount = -10;
        }

        switch (event.getSlot()) {
            case 0 -> {
                Inventory slotActionMenuInventory = new SlotActionMenuInventory(this.getInteractiveInventory()).buildInventory();
                player.openInventory(slotActionMenuInventory);
            }
            case 3 -> {
                this.removeAmount += amount;
                this.removeAmount = Math.max(this.removeAmount, 1);
                this.removeAmount = Math.min(this.removeAmount, 64);
                setRemoveAmount();
            }
            case 4 -> {
                this.minimumAmount += amount;
                this.minimumAmount = Math.max(this.minimumAmount, 1);
                this.minimumAmount = Math.min(this.minimumAmount, 64);
                setMinimumAmount();
            }
            case 8 -> {
                this.getInteractiveInventory().addSlotAction(slot, getClickAction(), new RemoveItemAction(minimumAmount, removeAmount));
                Inventory slotActionMenuInventory = new SlotActionMenuInventory(this.getInteractiveInventory()).buildInventory();
                player.openInventory(slotActionMenuInventory);
            }
        }
    }

    public void setRemoveAmount() {
        this.inventory.setItem(3, new ItemBuilder(Material.REDSTONE).setName("§3Remove amount§7: §b" + this.removeAmount).build());
    }

    public void setMinimumAmount() {
        this.inventory.setItem(4, new ItemBuilder(Material.REDSTONE_BLOCK).setName("§3Minimum amount§7: §b" + this.minimumAmount).build());
    }

}
