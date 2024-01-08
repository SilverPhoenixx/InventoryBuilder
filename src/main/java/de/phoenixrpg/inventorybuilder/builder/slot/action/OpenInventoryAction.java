package de.phoenixrpg.inventorybuilder.builder.slot.action;

import com.google.gson.annotations.Expose;
import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.builder.InventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

public class OpenInventoryAction extends SlotAction {

    @Expose
    private String key;
    public OpenInventoryAction(String key) {
        this.key = key;
    }

    @Override
    public String getName() {
        return "OpenInventory";
    }

    @Override
    public void runAction(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        InventoryManager inventoryManager = InventoryBuilder.getInstance().getInventoryManager();

        Optional<InteractiveInventory> optionalInventory = inventoryManager.getInventoryByKey(this.key);
        if(optionalInventory.isEmpty()) {
            player.sendMessage("§7>§c The inventory with the key§7:§c " + this.key + " §cwas not found.");
            return;
        }

        player.openInventory(optionalInventory.get().buildInventory());
    }
}
