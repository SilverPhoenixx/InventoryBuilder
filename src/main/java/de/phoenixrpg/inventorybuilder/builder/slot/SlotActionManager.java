package de.phoenixrpg.inventorybuilder.builder.slot;

import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.action.*;
import de.phoenixrpg.inventorybuilder.inventories.slot.SlotActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.slot.actions.CloseActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.slot.actions.OpenActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.slot.actions.add.AddActionSelectorInventory;
import de.phoenixrpg.inventorybuilder.inventories.slot.actions.remove.RemoveActionSelectorInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import oshi.util.tuples.Triplet;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public class SlotActionManager {

    private final HashMap<Class<? extends SlotAction>, Triplet<String, Class<? extends SlotActionInventory>, ItemStack>> slotActionContainers;

    private final InventoryBuilder instance;

    public SlotActionManager(InventoryBuilder instance) {
        this.instance = instance;

        this.slotActionContainers = new HashMap<>();
    }

    /**
     * loading all SlotActions
     */
    public void loadSlotActions() {
        addSlotAction(OpenInventoryAction.class, OpenActionInventory.class, "OpenInventoryAction", new ItemBuilder(Material.TORCH).setName("§3Open Inventory Action").build());
        addSlotAction(CloseInventoryAction.class, CloseActionInventory.class, "CloseInventoryAction", new ItemBuilder(Material.LANTERN).setName("§3Close Inventory Action").build());
        addSlotAction(AddItemAction.class, AddActionSelectorInventory.class, "AddAmountAction", new ItemBuilder(Material.EMERALD).setName("§3Add Amount Action").build());
        addSlotAction(RemoveItemAction.class, RemoveActionSelectorInventory.class, "RemoveAmountAction", new ItemBuilder(Material.REDSTONE).setName("§3Remove Amount Action").build());
    }

    /**
     * Add a custom slot action to the plugin
     * @param actionClass of {@link SlotAction}
     * @param inventoryClass of {@link SlotActionInventory} that should be used as start / configuration inventory for given SlotAction
     * @param name of the given SlotAction for converting into json
     * @param itemStack of the given SlotAction that representing it in the {@link de.phoenixrpg.inventorybuilder.inventories.slot.overview.SlotActionOverviewInventory}
     * @return {@code true} if the SlotAction was successfully added, otherwise {@code true}
     */
    public boolean addSlotAction(Class<? extends SlotAction> actionClass, Class<? extends SlotActionInventory> inventoryClass, String name, ItemStack itemStack) {
        if (this.slotActionContainers.containsKey(inventoryClass)) return false;
        this.slotActionContainers.put(actionClass, new Triplet<>(name, inventoryClass, itemStack));
        return true;
    }

    /**
     * Get the amount of the inventories
     *
     * @return {@code int}
     */
    public int getAmountOfInventories() {
        return this.slotActionContainers.size();
    }

    /**
     * Creates a new instance of SlotActionInventory by given index
     * @param index                position of the inventory in the list
     * @param interactiveInventory the inventory to be edited
     * @param clickAction the selected click action
     * @return {@link Optional<SlotActionInventory>} when an SlotActionInventory was successfully created, otherwise Optional.empty()
     */
    public Optional<SlotActionInventory> getInventoryByIndex(int index, InteractiveInventory interactiveInventory, ClickAction clickAction) {
        if (index < 0) return Optional.empty();
        if (index >= this.slotActionContainers.size()) return Optional.empty();
        try {

            Class<? extends SlotAction> key = this.slotActionContainers.keySet().stream().toList().get(index);

            SlotActionInventory slotActionInventory = (SlotActionInventory) this.slotActionContainers
                    .get(key)
                    .getB()
                    .getDeclaredConstructors()[0]
                    .newInstance(interactiveInventory, clickAction);
            return Optional.of(slotActionInventory);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return Optional.empty();
        }
    }

    /**
     * Get the Triplet container by given key
     * @param clazz as key of needed triplet container
     * @return {@link Optional<Triplet>} if the container was successfully found, otherwise Optional.empty()
     */
    public Optional<Triplet<String, Class<? extends SlotActionInventory>, ItemStack>> getContainerByClass(Class<? extends SlotAction> clazz) {
        if(!this.slotActionContainers.containsKey(clazz)) return Optional.empty();

        return Optional.of(this.slotActionContainers.get(clazz));
    }

    /**
     * Get the ItemStack by given index
     * @param index of the needed itemstack
     * @return {@link Optional<ItemStack>}
     */
    public Optional<ItemStack> getItemStackByIndex(int index) {
        if (index < 0) return Optional.empty();
        if (index >= this.slotActionContainers.size()) return Optional.empty();
        return Optional.of(this.slotActionContainers
                .values()
                .stream()
                .toList()
                .get(index).getC());
    }

    /**
     * Get all classes of SlotActions
     * @return {@link Set<Class>}
     */
    public Set<Class<? extends SlotAction>> getClasses() {
        return this.slotActionContainers.keySet();
    }
}
