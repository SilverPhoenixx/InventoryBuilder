package de.phoenixrpg.inventorybuilder.inventories.slot;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.inventories.EditorInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.ClickAction;

public abstract class SlotActionInventory extends EditorInventory {

    private ClickAction clickAction;
    public SlotActionInventory(InteractiveInventory interactiveInventory, ClickAction clickAction) {
        super(interactiveInventory);
        this.clickAction = clickAction;
    }

    public ClickAction getClickAction() {
        return clickAction;
    }

    public void setClickAction(ClickAction clickAction) {
        this.clickAction = clickAction;
    }
}
