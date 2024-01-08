package de.phoenixrpg.inventorybuilder.inventories.slot.actions;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import de.phoenixrpg.inventorybuilder.builder.items.action.ItemAction;
import de.phoenixrpg.inventorybuilder.builder.items.action.SetItemAction;
import de.phoenixrpg.inventorybuilder.adapter.PhoenixCraftInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.action.SlotAction;
import de.phoenixrpg.inventorybuilder.inventories.slot.SlotActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.menu.SlotActionMenuInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;

public class DeleteActionInventory extends SlotActionInventory {
    public DeleteActionInventory(InteractiveInventory interactiveInventory, ClickAction action) {
        super(interactiveInventory, action);
    }

    @Override
    public Inventory buildInventory() {
        if (this.getInteractiveInventory().getInventoryType() == InventoryType.CHEST) {
            this.inventory = Bukkit.createInventory(this, this.getInteractiveInventory().getSize(), "§3Delete Slot Action");
        } else {
            this.inventory = new PhoenixCraftInventory(this, this.getInteractiveInventory().getInventoryType(), "§3Close Slot Action");
        }

        for (ItemAction itemAction : this.getInteractiveInventory().getItemActions()) {
            if (!(itemAction instanceof SetItemAction)) continue;
            itemAction.runAction(this.inventory);
        }

        for(int slot : this.getInteractiveInventory().getSlotActions().keySet()) {
            ItemStack item = this.inventory.getItem(slot);

            if(item == null) continue;

            ItemBuilder builder = new ItemBuilder(item);

            ArrayList<String> lore = new ArrayList<>();
            for(Map.Entry<ClickAction, SlotAction> entry : this.getInteractiveInventory().getSlotActions().get(slot).entrySet()) {
                lore.add("§7>§3 Click §7:§b " + entry.getKey() + " §3Action§7: §b" + entry.getValue().getName());
            }
            builder.setList(lore);

            this.inventory.setItem(slot, builder.build());
        }

        return this.inventory;
    }

    @Override
    public void runInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        delete(event.getSlot());
        openInventory(player);
    }


    public void delete(int slot) {
        this.getInteractiveInventory().removeSlotAction(slot);
    }

    public void openInventory(Player player) {
        Inventory slotActionMenuInventory = new SlotActionMenuInventory(this.getInteractiveInventory()).buildInventory();
        player.openInventory(slotActionMenuInventory);
    }
}
