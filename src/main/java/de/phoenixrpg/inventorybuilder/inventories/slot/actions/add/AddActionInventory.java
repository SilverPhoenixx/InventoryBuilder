package de.phoenixrpg.inventorybuilder.inventories.slot.actions.add;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import de.phoenixrpg.inventorybuilder.builder.slot.action.AddItemAction;
import de.phoenixrpg.inventorybuilder.inventories.menu.SlotActionMenuInventory;
import de.phoenixrpg.inventorybuilder.inventories.slot.SlotActionInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class AddActionInventory extends SlotActionInventory {


    private final int slot;

    private int addAmount;
    private int maximumAmount;

    public AddActionInventory(InteractiveInventory interactiveInventory, ClickAction clickAction, int slot) {
        super(interactiveInventory, clickAction);

        this.slot = slot;

        this.addAmount = 1;
        this.maximumAmount = 1;
    }

    @Override
    public Inventory buildInventory() {
        this.inventory = Bukkit.createInventory(this, 9, "§3Add Item Action");

        this.fillItem(0, 9, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());

        this.inventory.setItem(0, new ItemBuilder(Material.LIGHT_GRAY_SHULKER_BOX).setName("§7Back to menu").build());

        this.setAddAmount();
        this.setMaximumAmount();

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
                this.addAmount += amount;
                this.addAmount = Math.max(this.addAmount, 1);
                this.addAmount = Math.min(this.addAmount, 64);
                setAddAmount();
            }
            case 4 -> {
                this.maximumAmount += amount;
                this.maximumAmount = Math.max(this.maximumAmount, 1);
                this.maximumAmount = Math.min(this.maximumAmount, 64);
                setMaximumAmount();
            }
            case 8 -> {
                this.getInteractiveInventory().addSlotAction(slot, getClickAction(), new AddItemAction(maximumAmount, addAmount));
                Inventory slotActionMenuInventory = new SlotActionMenuInventory(this.getInteractiveInventory()).buildInventory();
                player.openInventory(slotActionMenuInventory);
            }
        }
    }

    public void setAddAmount() {
        this.inventory.setItem(3, new ItemBuilder(Material.EMERALD).setName("§3Add amount§7: §b" + this.addAmount).build());
    }

    public void setMaximumAmount() {
        this.inventory.setItem(4, new ItemBuilder(Material.EMERALD_BLOCK).setName("§3Maximum amount§7: §b" + this.maximumAmount).build());
    }

}
