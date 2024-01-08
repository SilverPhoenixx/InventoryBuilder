package de.phoenixrpg.inventorybuilder.inventories.menu;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import de.phoenixrpg.inventorybuilder.inventories.PhoenixInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class TypeMenuInventory extends PhoenixInventory {

    private InteractiveInventory interactiveInventory;

    public TypeMenuInventory(InteractiveInventory inventoryTypeInventory) {
        this.interactiveInventory = inventoryTypeInventory;
    }
    @Override
    public Inventory buildInventory() {
        this.inventory = Bukkit.createInventory(this, 36, "§3Inventory Settings §7>§b " + interactiveInventory.getKey());

        fillItem(0, 36, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());

        this.inventory.setItem(0,  new ItemBuilder(Material.LIGHT_GRAY_SHULKER_BOX).setName("§cBack to main menu").build());

        updateInformation();

        this.inventory.setItem(9, new ItemBuilder(Material.CHEST).setName("§3Chest").build());
        this.inventory.setItem(10, new ItemBuilder(Material.DISPENSER).setName("§3Dispenser").build());
        this.inventory.setItem(11, new ItemBuilder(Material.FURNACE).setName("§3Furnace").build());
        this.inventory.setItem(12, new ItemBuilder(Material.CRAFTING_TABLE).setName("§3Crafting table").build());
        this.inventory.setItem(13, new ItemBuilder(Material.ENCHANTING_TABLE).setName("§3Enchanting table").build());
        this.inventory.setItem(14, new ItemBuilder(Material.BREWING_STAND).setName("§3Brewing stand").build());
        this.inventory.setItem(15, new ItemBuilder(Material.ANVIL).setName("§3Anvil").build());
        this.inventory.setItem(16, new ItemBuilder(Material.SMITHING_TABLE).setName("§3Smithing table").build());
        this.inventory.setItem(17, new ItemBuilder(Material.HOPPER).setName("§3Hopper").build());
        this.inventory.setItem(18, new ItemBuilder(Material.LECTERN).setName("§3Lectern").build());

        return this.inventory;
    }

    public void updateInformation() {
        this.inventory.setItem(4, new ItemBuilder(Material.CHEST).setName("§3Inventory information")
                .setList("§3Type§7:§b " + this.interactiveInventory.getInventoryType().name(),
                        "§3Size§7:§b " + this.interactiveInventory.getSize())
                .build());
    }

    @Override
    public void runInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        switch (event.getSlot()) {
            case 0 -> {
                Inventory menuInventory = new MainMenuInventory(interactiveInventory).buildInventory();
                player.openInventory(menuInventory);
            }
            case 4 -> this.interactiveInventory.addSize();
            case 9 -> this.interactiveInventory.setInventoryType(InventoryType.CHEST);
            case 10 -> this.interactiveInventory.setInventoryType(InventoryType.DISPENSER);
            case 11 -> this.interactiveInventory.setInventoryType(InventoryType.FURNACE);
            case 12 -> this.interactiveInventory.setInventoryType(InventoryType.WORKBENCH);
            case 13 -> this.interactiveInventory.setInventoryType(InventoryType.ENCHANTING);
            case 14 -> this.interactiveInventory.setInventoryType(InventoryType.BREWING);
            case 15 -> this.interactiveInventory.setInventoryType(InventoryType.ANVIL);
            case 16 -> this.interactiveInventory.setInventoryType(InventoryType.SMITHING);
            case 17 -> this.interactiveInventory.setInventoryType(InventoryType.HOPPER);
            case 18 -> this.interactiveInventory.setInventoryType(InventoryType.LECTERN);
        }
        updateInformation();
    }
}
