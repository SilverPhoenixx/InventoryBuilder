package de.phoenixrpg.inventorybuilder.inventories.menu;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import de.phoenixrpg.inventorybuilder.inventories.EditorInventory;
import de.phoenixrpg.inventorybuilder.inventories.item.overview.ItemOverviewInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MainMenuInventory extends EditorInventory {

    public MainMenuInventory(InteractiveInventory interactiveInventory) {
        super(interactiveInventory);
    }
    
    @Override
    public Inventory buildInventory() {
        this.inventory = Bukkit.createInventory(this, 9, "§7>§3 Menu §7>§b " + this.getInteractiveInventory().getKey());

        fillItem(0, 9, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());

        this.inventory.setItem(3,
                new ItemBuilder(Material.POLISHED_BLACKSTONE_BUTTON)
                        .setName("§7>§3 Inventory Builder")
                        .build());

        this.inventory.setItem(4,
                new ItemBuilder(Material.COMPARATOR)
                        .setName("§7>§3 Action Builder")
                        .build());

        this.inventory.setItem(5,
                new ItemBuilder(Material.CHEST)
                        .setName("§7>§3 Inventory Type")
                        .build());

        this.inventory.setItem(7,
                new ItemBuilder(Material.ENDER_CHEST)
                        .setName("§7>§3 Open Inventory")
                        .build());

        return this.inventory;
    }

    @Override
    public void runInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 3 -> {
                Inventory itemActionInventory = new ItemOverviewInventory(this.getInteractiveInventory()).buildInventory();
                player.openInventory(itemActionInventory);
            }
            case 4 -> {
                Inventory slotActionInventory = new SlotActionMenuInventory(this.getInteractiveInventory()).buildInventory();
                player.openInventory(slotActionInventory);
            }
            case 5 -> {
                Inventory inventoryTypeInventory = new TypeMenuInventory(this.getInteractiveInventory()).buildInventory();
                player.openInventory(inventoryTypeInventory);
            }
            case 7 -> {
                Inventory builtInventory = this.getInteractiveInventory().buildInventory();
                player.openInventory(builtInventory);
            }
        }
    }
}
