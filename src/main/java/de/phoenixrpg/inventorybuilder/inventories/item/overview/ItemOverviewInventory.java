package de.phoenixrpg.inventorybuilder.inventories.item.overview;

import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import de.phoenixrpg.inventorybuilder.inventories.item.ItemActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.menu.MainMenuInventory;
import de.phoenixrpg.inventorybuilder.builder.items.ItemActionManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ItemOverviewInventory extends ItemActionInventory {

    private int page;

    public ItemOverviewInventory(InteractiveInventory interactiveInventory) {
        super(interactiveInventory);
        this.page = 1;
    }

    @Override
    public Inventory buildInventory() {
        this.inventory = Bukkit.createInventory(this, 54, "§3Item Action Overview");
        for (int slot = 0; slot < 9; slot++) {
            this.inventory.setItem(slot, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());
        }

        for (int slot = 45; slot < 54; slot++) {
            this.inventory.setItem(slot, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());
        }

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
    public void loadCurrentActions() {
        ItemActionManager itemInventoryManager = InventoryBuilder.getInstance().getItemActionManager();

        clearActions();

        if ((page - 1) * 36 > itemInventoryManager.getAmountOfInventories()) return;

        int from = (page - 1) * 36;
        int to = Math.min(page * 36, itemInventoryManager.getAmountOfInventories());

        int currentSlot = 9;
        for (int pos = from; pos < to; pos++) {
            Optional<ItemStack> optionalItemStack = itemInventoryManager.getItemStackByIndex(pos);

            if (optionalItemStack.isEmpty()) break;

            ItemStack currentItem = optionalItemStack.get().clone();
            this.inventory.setItem(currentSlot, currentItem);
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
        this.loadCurrentActions();
    }

    @Override
    public void runInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getSlot() >= 9 && event.getSlot() <= 44) {
            ItemActionManager itemInventoryManager = InventoryBuilder.getInstance().getItemActionManager();
            int index = ((page - 1) * 36) + event.getSlot()-9;
            Optional<ItemActionInventory> optionalInventory = itemInventoryManager.getInventoryByIndex(index, this.getInteractiveInventory());
            if(optionalInventory.isEmpty()) return;

            player.openInventory(optionalInventory.get().buildInventory());
            return;
        }
        switch (event.getSlot()) {
            case 47 -> {
                if(this.page == 1) return;
                this.page--;
                updateInventory();
            }
            case 49 -> {
                Inventory mainMenuInventory = new MainMenuInventory(this.getInteractiveInventory()).buildInventory();
                player.openInventory(mainMenuInventory);
            }
            case 51 -> {
                this.page++;
                updateInventory();
            }
        }
    }
}
