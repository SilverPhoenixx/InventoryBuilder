package de.phoenixrpg.inventorybuilder.builder.slot;

import org.jetbrains.annotations.NotNull;

public enum ClickAction {
    /**
     * There is an enum called {@link org.bukkit.event.inventory.ClickType}
     * but that don't contain "ALL"
     */

    ALL,
    RIGHT,
    LEFT,
    SHIFT_RIGHT,
    SHIFT_LEFT,
    MIDDLE;

    /**
     * Get the next possible ClickAction
     * Is there no next possible ClickAction returning {@code ClickAction.ALL}
     * @return {@code ClickAction}
     */
    @NotNull
    public ClickAction nextClickType() {
        int nextIndex = this.ordinal() + 1;
        for(ClickAction clickAction : values()) {
            if(clickAction.ordinal() == nextIndex) return clickAction;
        }
        return ALL;
    }

    /**
     * Get click action by name
     * @param name of click action
     * @return {@code ClickAction} when there is a enum with the name, otherwise always {@code ALL}
     */
    @NotNull
    public static ClickAction byName(@NotNull String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException ignored) {
            return ALL;
        }
    }
}
