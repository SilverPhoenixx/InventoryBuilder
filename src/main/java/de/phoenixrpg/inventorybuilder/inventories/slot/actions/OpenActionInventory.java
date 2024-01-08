package de.phoenixrpg.inventorybuilder.inventories.slot.actions;

import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import de.phoenixrpg.inventorybuilder.builder.slot.action.OpenInventoryAction;
import de.phoenixrpg.inventorybuilder.inventories.slot.SlotActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.menu.SlotActionMenuInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.ClickAction;
import de.phoenixrpg.inventorybuilder.inventories.consumer.SlotConsumerInventory;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Collections;

public class OpenActionInventory extends SlotActionInventory {


    public OpenActionInventory(InteractiveInventory interactiveInventory, ClickAction action) {
        super(interactiveInventory, action);
    }
    @Override
    public Inventory buildInventory() {
        SlotConsumerInventory consumerInventory = new SlotConsumerInventory(getInteractiveInventory(), getClickAction(), "§3Select Slot", event -> {
            openText((Player) event.getWhoClicked(), event.getSlot());
        });
        return consumerInventory.buildInventory();
    }


    public void openText(Player player, int inventorySlot) {
        AnvilGUI.Builder builder = new AnvilGUI.Builder();

        builder.onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();
                    return Arrays.asList(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> {
                                openInventory(player);
                                if (stateSnapshot.getOutputItem() == null) return;
                                if (stateSnapshot.getOutputItem().getType() == Material.AIR) return;
                                save(stateSnapshot.getText(), inventorySlot);
                            }));
                })
                .onClose(stateSnapshot -> {
                    openInventory(player);
                    if (stateSnapshot.getOutputItem() == null) return;
                    if (stateSnapshot.getOutputItem().getType() == Material.AIR) return;
                    save(stateSnapshot.getText(), inventorySlot);
                })
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .title("§3Set target inventory")
                .itemLeft(new ItemBuilder(Material.PAPER).build())
                .plugin(InventoryBuilder.getInstance())
                .open(player);
    }

    public void save(String text, int slot) {
        this.getInteractiveInventory().addSlotAction(slot, this.getClickAction(), new OpenInventoryAction(text));
    }

    public void openInventory(Player player) {
        Inventory slotActionMenuInventory = new SlotActionMenuInventory(this.getInteractiveInventory()).buildInventory();
        player.openInventory(slotActionMenuInventory);
    }
}
