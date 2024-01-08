package de.phoenixrpg.inventorybuilder.inventories.item.overview;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import de.phoenixrpg.inventorybuilder.builder.items.action.FillItemAction;

import de.phoenixrpg.inventorybuilder.inventories.item.ItemActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.item.actions.FillItemActionInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class FillItemActionOverviewInventory extends ItemActionInventory {

    private int page;
    private ArrayList<FillItemAction> fillItemActions;
    public FillItemActionOverviewInventory(InteractiveInventory interactiveInventory) {
        super(interactiveInventory);
        this.page = 1;

        this.fillItemActions = getFillItemActions();
    }

    @Override
    public Inventory buildInventory() {
        this.inventory = Bukkit.createInventory(this, 54, "§3Fill Item Action Overview");

        for(int slot = 0; slot < 9; slot++) {
            this.inventory.setItem(slot, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());
            this.inventory.setItem(45 + slot, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§3").build());
        }

        setFillItems();

        this.inventory.setItem(4, new ItemBuilder(Material.LIME_SHULKER_BOX).setName("§aAdd fill item action").build());
        this.inventory.setItem(49, new ItemBuilder(Material.RED_SHULKER_BOX).setName("§cBack to main menu").build());

        return this.inventory;
    }

    public void clearFillItems() {
        for(int slot = 9; slot < 45; slot++) {
            this.inventory.setItem(slot, new ItemBuilder(Material.AIR).build());
        }
    }
    public void setFillItems() {


        int slot = 9;
        for(FillItemAction fillItemAction : fillItemActions) {
            this.inventory.setItem(slot, new ItemBuilder(fillItemAction.getItemStack().clone())
                    .setName("§3Fill Item Action")
                    .setList(
                            "§7>§3 Priority§7:§b " + fillItemAction.getPriority(),
                            "§7>§3 From§7:§b " + fillItemAction.getFrom(),
                            "§7>§3 To§7:§b " + fillItemAction.getTo(),
                            "§7>§3 Step§7:§b " + fillItemAction.getStep(),
                            "§3",
                            "§7>§3 Right click to §bREMOVE §3the action"
                    ).build());

            slot++;

            if(slot == 45) break;
        }
    }

    @Override
    public void runInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();


        if(event.getSlot() == 4) {
            Inventory fillItemInventory = new FillItemActionInventory(this.getInteractiveInventory()).buildInventory();
            player.openInventory(fillItemInventory);
            return;
        }

        if(event.getSlot() == 49) {
            Inventory itemActionMenu = new ItemOverviewInventory(this.getInteractiveInventory()).buildInventory();
            player.openInventory(itemActionMenu);
            return;
        }

        if(event.getSlot() > 8 && event.getSlot() < 45) {
            if(event.getSlot() - 9 > fillItemActions.size()-1) return;

            FillItemAction fillItemAction = fillItemActions.get(event.getSlot() - 9);
            if(fillItemAction == null) return;

            if(event.getClick() == ClickType.RIGHT) {
                this.getInteractiveInventory().removeItemAction(fillItemAction);
                this.fillItemActions = getFillItemActions();
                this.clearFillItems();
                this.setFillItems();
            } else {
                Inventory fillItemActionInventory = new FillItemActionInventory(this.getInteractiveInventory(), fillItemAction).buildInventory();
                player.openInventory(fillItemActionInventory);
            }
        }
    }

    public ArrayList<FillItemAction> getFillItemActions() {
        ArrayList<FillItemAction> fillItems = new ArrayList<>();
        this.getInteractiveInventory().getItemActions().
                forEach(itemAction -> {
            if(!(itemAction instanceof FillItemAction fillItemAction)) return;
            fillItems.add(fillItemAction);
        });

        return fillItems;
        }
}
