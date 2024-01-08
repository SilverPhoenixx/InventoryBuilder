package de.phoenixrpg.inventorybuilder.inventories.slot.overview;

import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import de.phoenixrpg.inventorybuilder.inventories.slot.SlotActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.menu.SlotActionMenuInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.SlotActionManager;
import de.phoenixrpg.inventorybuilder.builder.slot.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class SlotActionOverviewInventory extends SlotActionInventory {

    private int page;

    public SlotActionOverviewInventory(InteractiveInventory interactiveInventory, ClickAction clickAction) {
        super(interactiveInventory, clickAction);
        this.page = 1;
    }

    @Override
    public Inventory buildInventory() {
        this.inventory = Bukkit.createInventory(this, 54, "§3Slot Action Overview");

        fillItem(0, 9, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());
        fillItem(45, 54, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());
        updateInventory();

        this.inventory.setItem(49, new ItemBuilder(Material.LIGHT_GRAY_SHULKER_BOX).setName("§7Back to menu").build());

        return this.inventory;
    }

    public void clearActions() {
        for (int slot = 9; slot < 44; slot++) {
            this.inventory.setItem(slot, new ItemBuilder(Material.AIR).build());
        }
    }

    public void setInformationItem() {
        this.inventory.setItem(4, new ItemBuilder(Material.PAPER).setName("§3Information").setList(
                "§3Page§7:§b " + this.page,
                "§3Action§7:§b " + this.getClickAction()
        ).build());
    }

    public void updateInventory() {
        this.setInformationItem();
        this.setPageSelector();
        this.loadCurrentActions();
    }


    public void loadCurrentActions() {
        SlotActionManager slotInventoryManager = InventoryBuilder.getInstance().getSlotActionManager();

        clearActions();

        if ((page - 1) * 36 > slotInventoryManager.getAmountOfInventories()) return;

        int from = (page - 1) * 36;
        int to = Math.min(page * 36, slotInventoryManager.getAmountOfInventories());

        int currentSlot = 9;
        for (int pos = from; pos < to; pos++) {
            Optional<ItemStack> optionalItemStack = slotInventoryManager.getItemStackByIndex(pos);

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

    @Override
    public void runInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() >= 9 && event.getSlot() <= 44) {
            SlotActionManager slotInventoryManager = InventoryBuilder.getInstance().getSlotActionManager();
            int index = ((page - 1) * 36) + event.getSlot() - 9;
            Optional<SlotActionInventory> optionalInventory = slotInventoryManager.getInventoryByIndex(index, this.getInteractiveInventory(), this.getClickAction());
            if (optionalInventory.isEmpty()) return;

            player.openInventory(optionalInventory.get().buildInventory());
            return;
        }
        switch (event.getSlot()) {
            case 4 -> {
                setClickAction(getClickAction().nextClickType());
                setInformationItem();
            }
            case 47 -> {
                if (page == 1) return;
                page--;
                updateInventory();
            }
            case 49 -> {
                Inventory slotMenu = new SlotActionMenuInventory(this.getInteractiveInventory()).buildInventory();
                player.openInventory(slotMenu);
            }
            case 51 -> {
                page++;
                updateInventory();
            }
        }
    }
}
