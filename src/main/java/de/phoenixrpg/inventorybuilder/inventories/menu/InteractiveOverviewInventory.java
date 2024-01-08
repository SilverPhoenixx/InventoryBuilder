package de.phoenixrpg.inventorybuilder.inventories.menu;

import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.builder.InventoryManager;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import de.phoenixrpg.inventorybuilder.inventories.PhoenixInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

public class InteractiveOverviewInventory extends PhoenixInventory {

    private int page;

    public InteractiveOverviewInventory() {
        this.page = 1;
    }

    @Override
    public Inventory buildInventory() {
        this.inventory = Bukkit.createInventory(this, 54, "§3Interactive Inventory Overview");
        fillItem(0, 9, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());
        fillItem(45, 54, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());

        updateInventory();

        this.inventory.setItem(49, new ItemBuilder(Material.LIGHT_GRAY_SHULKER_BOX).setName("§7Back to menu").build());

        return this.inventory;
    }

    public void setPageItem() {
        this.inventory.setItem(4, new ItemBuilder(Material.PAPER).setName("§3Current Page§7:§b " + this.page).build());
    }
    public void clearActions() {
        for(int slot = 9; slot < 44; slot++) {
            this.inventory.setItem(slot, new ItemBuilder(Material.AIR).build());
        }
    }
    public void loadCurrentInventories() {
        InventoryManager itemInventoryManager = InventoryBuilder.getInstance().getInventoryManager();

        clearActions();

        if ((page - 1) * 36 > itemInventoryManager.getAmountOfInventories()) return;

        int from = (page - 1) * 36;
        int to = Math.min(page * 36, itemInventoryManager.getAmountOfInventories());

        int currentSlot = 9;
        for (int pos = from; pos < to; pos++) {
            Optional<InteractiveInventory> optionalInteractiveInventory = itemInventoryManager.getInventoryByIndex(pos);

            if (optionalInteractiveInventory.isEmpty()) break;

            InteractiveInventory interactiveInventory = optionalInteractiveInventory.get();
            this.inventory.setItem(currentSlot, new ItemBuilder(Material.PAPER).setName("§7" + interactiveInventory.getKey())
                    .setList(
                            "§3Title§7:§f " + interactiveInventory.getTitle(),
                            "§3Size§7: §b" + interactiveInventory.getSize(),
                            "§3Inventory Type§7: §b" + interactiveInventory.getInventoryType().name(),
                            "§3",
                            "§3Left click to edit",
                            "§3Shift right click to remove").build());
            currentSlot++;
        }
    }

    public void setPageSelector() {
        if (page > 1) {
            this.inventory.setItem(47, new ItemBuilder(Material.ARROW).setName("§3Previous page").build());
        } else {
            this.inventory.setItem(47, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());
        }

        this.inventory.setItem(51, new ItemBuilder(Material.ARROW).setName("§3Next page").build());
    }

    public void updateInventory() {
        this.setPageItem();
        this.setPageSelector();
        this.loadCurrentInventories();
    }

    @Override
    public void runInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getSlot() >= 9 && event.getSlot() <= 44) {
            InventoryManager inventoryManager = InventoryBuilder.getInstance().getInventoryManager();
            int index = ((page - 1) * 36) + event.getSlot()-9;

            if(index >= inventoryManager.getAmountOfInventories()) return;
            Optional<InteractiveInventory> optionalInventory = inventoryManager.getInventoryByIndex(index);
            if(optionalInventory.isEmpty()) return;

            if(event.getClick() == ClickType.SHIFT_RIGHT) {
                inventoryManager.removeInventory(optionalInventory.get().getKey());
                updateInventory();
                player.sendMessage("§3The inventory§7: §b" + optionalInventory.get().getKey() + " §3got removed.");
            } else if(event.getClick() == ClickType.LEFT) {
                Inventory menuInventory = new MainMenuInventory(optionalInventory.get()).buildInventory();
                player.openInventory(menuInventory);
            }
            return;
        }
        switch (event.getSlot()) {
            case 47 -> {
                if(this.page == 1) return;
                this.page--;
                updateInventory();
            }
            case 51 -> {
                this.page++;
                updateInventory();
            }
        }
    }
}
