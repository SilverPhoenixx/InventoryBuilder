package de.phoenixrpg.inventorybuilder.builder.items;

import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.builder.items.action.FillItemAction;
import de.phoenixrpg.inventorybuilder.builder.items.action.ItemAction;
import de.phoenixrpg.inventorybuilder.builder.items.action.SetItemAction;
import de.phoenixrpg.inventorybuilder.builder.slot.action.SlotAction;
import de.phoenixrpg.inventorybuilder.inventories.item.ItemActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.item.actions.SetItemActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.item.overview.FillItemActionOverviewInventory;
import de.phoenixrpg.inventorybuilder.inventories.slot.SlotActionInventory;
import de.phoenixrpg.inventorybuilder.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import oshi.util.tuples.Triplet;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public class ItemActionManager {

    private final HashMap<Class<? extends ItemAction>, Triplet<String, Class<? extends ItemActionInventory>, ItemStack>> itemActionContainers;

    private final InventoryBuilder instance;

    public ItemActionManager(InventoryBuilder instance) {
        this.instance = instance;

        this.itemActionContainers = new HashMap<>();
    }

    /**
     * loading all ItemActions
     */
    public void loadItemActions() {
        addItemAction(SetItemAction.class, SetItemActionInventory.class, "SetAction", new ItemBuilder(Material.POLISHED_BLACKSTONE_BUTTON)
                .setName("§3Set Item Action")
                .setList("§3Middle click on slot with item on cursor",
                         "§3will duplicate the item to the clicked slot")
                .build());
        addItemAction(FillItemAction.class, FillItemActionOverviewInventory.class, "FillAction", new ItemBuilder(Material.POLISHED_BLACKSTONE).setName("§3Fill Item Action").build());
    }

    /**
     * Add a custom item action to the plugin
     * @param actionClass of {@link ItemAction}
     * @param inventoryClass of {@link ItemActionInventory} that should be used as start / configuration inventory for given ItemAction
     * @param name of the given SlotAction for converting into json
     * @param itemStack of the given ItemAction that representing it in the {@link de.phoenixrpg.inventorybuilder.inventories.item.overview.ItemOverviewInventory}
     * @return {@code true} if the SlotAction was successfully added, otherwise {@code true}
     */
    public boolean addItemAction(Class<? extends ItemAction> actionClass, Class<? extends ItemActionInventory> inventoryClass, String name, ItemStack itemStack) {
        if (this.itemActionContainers.containsKey(inventoryClass)) return false;
        this.itemActionContainers.put(actionClass, new Triplet<>(name, inventoryClass, itemStack));
        return true;
    }

    /**
     * Get the amount of the inventories
     *
     * @return {@code int}
     */
    public int getAmountOfInventories() {
        return this.itemActionContainers.size();
    }

    /**
     * Creates a new instance of ItemActionInventory by given index
     * @param index                position of the inventory in the list
     * @param interactiveInventory the inventory to be edited
     * @return {@link Optional<ItemActionInventory>} when an ItemActionInventory was successfully created, otherwise Optional.empty()
     */
    public Optional<ItemActionInventory> getInventoryByIndex(int index, InteractiveInventory interactiveInventory) {
        if (index < 0) return Optional.empty();
        if (index >= this.itemActionContainers.size()) return Optional.empty();
        try {

            Class<? extends ItemAction> key = this.itemActionContainers.keySet().stream().toList().get(index);

            ItemActionInventory itemActionInventory = (ItemActionInventory) this.itemActionContainers
                    .get(key)
                    .getB()
                    .getDeclaredConstructors()[0]
                    .newInstance(interactiveInventory);
            return Optional.of(itemActionInventory);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            return Optional.empty();
        }
    }

    /**
     * Get the Triplet container by given key
     * @param clazz as key of needed triplet container
     * @return {@link Optional<Triplet>} if the container was successfully found, otherwise Optional.empty()
     */
    public Optional<Triplet<String, Class<? extends ItemActionInventory>, ItemStack>> getContainerByClass(Class<? extends ItemAction> clazz) {
        if(!this.itemActionContainers.containsKey(clazz)) return Optional.empty();

        return Optional.of(this.itemActionContainers.get(clazz));
    }

    /**
     * Get the ItemStack by given index
     * @param index of the needed itemstack
     * @return {@link Optional<ItemStack>}
     */
    public Optional<ItemStack> getItemStackByIndex(int index) {
        if (index < 0) return Optional.empty();
        if (index >= this.itemActionContainers.size()) return Optional.empty();
        return Optional.of(this.itemActionContainers
                .values()
                .stream()
                .toList()
                .get(index).getC());
    }
    /**
     * Get all classes of ItemActions
     * @return {@link Set<Class>}
     */
    public Set<Class<? extends ItemAction>> getClasses() {
        return this.itemActionContainers.keySet();
    }
}
