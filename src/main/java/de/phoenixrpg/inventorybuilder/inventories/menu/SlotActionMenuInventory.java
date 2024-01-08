package de.phoenixrpg.inventorybuilder.inventories.menu;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import de.phoenixrpg.inventorybuilder.inventories.slot.overview.SlotActionOverviewInventory;
import de.phoenixrpg.inventorybuilder.inventories.slot.SlotActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.slot.actions.DeleteActionInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class SlotActionMenuInventory extends SlotActionInventory {

    public SlotActionMenuInventory(InteractiveInventory interactiveInventory) {
        super(interactiveInventory, ClickAction.ALL);
    }

    @Override
    public Inventory buildInventory() {
        this.inventory = Bukkit.createInventory(this, 36, "§3Slot Action Menu §7>§b " + getInteractiveInventory().getKey());

        fillItem(0, 36, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());

        this.inventory.setItem(11,
                new ItemBuilder(Material.RED_SHULKER_BOX)
                        .setName("§cDelete action from item")
                        .build());

        this.inventory.setItem(15,
                new ItemBuilder(Material.LIME_SHULKER_BOX)
                        .setName("§aView slot actions")
                        .build());

        this.inventory.setItem(31,
                new ItemBuilder(Material.LIGHT_GRAY_SHULKER_BOX)
                        .setName("§7Back to main menu")
                        .build());

        this.setClickType();

        return this.inventory;
    }


    public void setClickType() {
        this.inventory.setItem(13, new ItemBuilder(Material.PAPER).setName("§3Click Action §7>§b " + this.getClickAction()).build());
    }

    @Override
    public void runInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 31 -> {
                Inventory menuInventory = new MainMenuInventory(this.getInteractiveInventory()).buildInventory();
                player.openInventory(menuInventory);
            }
            case 11 -> {
                Inventory deleteActionInventory = new DeleteActionInventory(this.getInteractiveInventory(), this.getClickAction()).buildInventory();
                player.openInventory(deleteActionInventory);
            }
            case 15 -> {
                Inventory deleteActionInventory = new SlotActionOverviewInventory(this.getInteractiveInventory(), this.getClickAction()).buildInventory();
                player.openInventory(deleteActionInventory);
            }
            case 13 -> {
                setClickAction(getClickAction().nextClickType());
                this.setClickType();
            }
        }
    }
}
