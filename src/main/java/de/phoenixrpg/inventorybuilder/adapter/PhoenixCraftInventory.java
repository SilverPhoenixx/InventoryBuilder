package de.phoenixrpg.inventorybuilder.adapter;

import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventoryCustom;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

/**
 * The class is necessary for calling onClose/onOpen because
 * the "normal" Bukkit.createInventoryInventory (example: Brewing stand)
 * don't call the InventoryCloseEvent with the right holder
 *
 * The Bukkit method creates {@link CraftInventoryCustom} with {@link InventoryHolder} equals null
 */
public class PhoenixCraftInventory extends CraftInventoryCustom {
    public PhoenixCraftInventory(InventoryHolder owner, InventoryType type, String title) {
        super(owner, type, title);
    }
}
