package de.phoenixrpg.inventorybuilder.inventories;

import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import org.jetbrains.annotations.NotNull;

/**
 * Class for creating interactive inventories based on polymorphism
 */
public abstract class EditorInventory extends PhoenixInventory {

    /**
     * InteractiveInventory that should be changed/edited
     */
    private final InteractiveInventory interactiveInventory;
    public EditorInventory(@NotNull InteractiveInventory interactiveInventory) {
        this.interactiveInventory = interactiveInventory;
    }

    @NotNull
    public InteractiveInventory getInteractiveInventory() {
        return interactiveInventory;
    }
}
