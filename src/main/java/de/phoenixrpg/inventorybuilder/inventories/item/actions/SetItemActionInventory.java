package de.phoenixrpg.inventorybuilder.inventories.item.actions;

import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.adapter.PhoenixCraftInventory;
import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.builder.items.action.ItemAction;
import de.phoenixrpg.inventorybuilder.builder.items.action.SetItemAction;
import de.phoenixrpg.inventorybuilder.inventories.item.ItemActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.item.overview.ItemOverviewInventory;
import de.phoenixrpg.inventorybuilder.inventories.menu.MainMenuInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class SetItemActionInventory extends ItemActionInventory {
    public SetItemActionInventory(InteractiveInventory interactiveInventory) {
        super(interactiveInventory);
    }

    @Override
    public Inventory buildInventory() {
        if (this.getInteractiveInventory().getInventoryType() == InventoryType.CHEST) {
            this.inventory = Bukkit.createInventory(this, this.getInteractiveInventory().getSize(), "§3Set Item Action");
        } else {
            this.inventory = new PhoenixCraftInventory(this, this.getInteractiveInventory().getInventoryType(), "§3Set Item Action");
        }

        for (ItemAction itemAction : this.getInteractiveInventory().getItemActions()) {
            if (!(itemAction instanceof SetItemAction)) continue;
            itemAction.runAction(this.inventory);
        }

        return this.inventory;
    }

    @Override
    public void runInteract(InventoryClickEvent event) {
        event.setCancelled(false);

        if(event.getClick() != ClickType.MIDDLE) return;
        if (event.getClickedInventory() == null) return;

        inventory.setItem(event.getSlot(), event.getCursor());
    }

    @Override
    public void runClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        ArrayList<ItemAction> currentActions = new ArrayList<>();
        for (ItemAction itemAction : this.getInteractiveInventory().getItemActions()) {
            if (itemAction instanceof SetItemAction) continue;
            currentActions.add(itemAction);
        }

        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            ItemStack itemStack = this.inventory.getItem(slot);
            if (itemStack == null) continue;
            if (itemStack.getType() == Material.AIR) continue;

            currentActions.add(new SetItemAction(itemStack.clone(), slot));
        }

        this.getInteractiveInventory().setItemActions(currentActions);

        Inventory menuInventory = new ItemOverviewInventory(this.getInteractiveInventory()).buildInventory();

        Bukkit.getScheduler().runTaskLater(InventoryBuilder.getInstance(), () -> player.openInventory(menuInventory), 2L);
    }


}
