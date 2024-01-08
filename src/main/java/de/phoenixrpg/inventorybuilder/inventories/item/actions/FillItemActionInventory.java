package de.phoenixrpg.inventorybuilder.inventories.item.actions;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import de.phoenixrpg.inventorybuilder.builder.items.action.FillItemAction;
import de.phoenixrpg.inventorybuilder.inventories.item.overview.FillItemActionOverviewInventory;
import de.phoenixrpg.inventorybuilder.inventories.item.ItemActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.item.overview.ItemOverviewInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class FillItemActionInventory extends ItemActionInventory {

    private final FillItemAction fillItemAction;

    public FillItemActionInventory(InteractiveInventory interactiveInventory) {
        super(interactiveInventory);
        this.fillItemAction = new FillItemAction(new ItemBuilder(Material.BARRIER).build(), 1, 1, 1, 1);
    }

    public FillItemActionInventory(InteractiveInventory interactiveInventory, FillItemAction fillItemAction) {
        super(interactiveInventory);
        this.fillItemAction = fillItemAction;
    }
    @Override
    public Inventory buildInventory() {
        this.inventory = Bukkit.createInventory(this, 27, "§3Fill Item Action");

        for(int slot = 0; slot < 27; slot++) {
            this.inventory.setItem(slot, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());
        }

        this.inventory.setItem(9, new ItemBuilder(Material.LIGHT_GRAY_SHULKER_BOX).setName("§cBack to menu").build());
        this.inventory.setItem(17, new ItemBuilder(Material.LIME_SHULKER_BOX).setName("§aSave changes").build());

        setPriority();
        setFrom();
        setTo();
        setStep();
        setItem();

        return this.inventory;
     }

     public void setItem() {
        this.inventory.setItem(15, fillItemAction.getItemStack());
     }

     public void setPriority() {
         this.inventory.setItem(4, new ItemBuilder(Material.REDSTONE_TORCH).setName("§3Priority §7> §b" + fillItemAction.getPriority()).build());
     }

    public void setStep() {
        this.inventory.setItem(13, new ItemBuilder(Material.BIRCH_PRESSURE_PLATE).setName("§3Step §7> §b" + fillItemAction.getStep()).build());
    }

     public void setTo() {
        this.inventory.setItem(12, new ItemBuilder(Material.GLOWSTONE).setName("§3To §7> §b" + fillItemAction.getTo()).build());
     }

    public void setFrom() {
        this.inventory.setItem(11, new ItemBuilder(Material.GLOWSTONE_DUST).setName("§3From §7> §b" + fillItemAction.getFrom()).build());
    }

    @Override
    public void runInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int value = 0;

        switch (event.getClick()) {
            case RIGHT -> value = 1;
            case SHIFT_RIGHT -> value = 10;
            case LEFT -> value = -1;
            case SHIFT_LEFT -> value = -10;
        }

        switch (event.getSlot()) {
            case 9 -> {
                Inventory itemActionMenuInventory = new ItemOverviewInventory(this.getInteractiveInventory()).buildInventory();
                player.openInventory(itemActionMenuInventory);
            }
            case 17 -> {
                this.getInteractiveInventory().addItemAction(fillItemAction);
                Inventory fillItemOverviewInventory = new FillItemActionOverviewInventory(this.getInteractiveInventory()).buildInventory();
                player.openInventory(fillItemOverviewInventory);
            }
            case 4 -> {
                fillItemAction.addPriority(value);
                setPriority();
            }
            case 11 -> {
                fillItemAction.addFrom(value);
                setFrom();
            }
            case 12 -> {
                fillItemAction.addTo(value);
                setTo();
            }
            case 13 -> {
                fillItemAction.addStep(value);
                setStep();
            }
            case 15 -> {
                if(event.getCursor() == null) return;
                if(event.getCursor().getType() == Material.AIR) return;
                fillItemAction.setItemStack(event.getCursor().clone());
                setItem();
            }
        }
    }
}
