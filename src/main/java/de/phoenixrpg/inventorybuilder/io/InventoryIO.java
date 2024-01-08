package de.phoenixrpg.inventorybuilder.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.adapter.ItemStackAdapter;
import de.phoenixrpg.inventorybuilder.adapter.RuntimeTypeAdapterFactory;
import de.phoenixrpg.inventorybuilder.builder.items.ItemActionManager;
import de.phoenixrpg.inventorybuilder.builder.items.action.ItemAction;
import de.phoenixrpg.inventorybuilder.builder.slot.SlotActionManager;
import de.phoenixrpg.inventorybuilder.builder.slot.action.SlotAction;
import de.phoenixrpg.inventorybuilder.inventories.item.ItemActionInventory;
import de.phoenixrpg.inventorybuilder.inventories.slot.SlotActionInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Triplet;

import java.util.Optional;

public class InventoryIO {

    private final InventoryBuilder instance;
    private Gson gson;

    public InventoryIO(InventoryBuilder instance) {
        this.instance = instance;
    }

    /**
     * Create the adapter dynamically from the @link{{@link SlotActionManager}}
     * with class and type name for the json object
     * @return @link{@link RuntimeTypeAdapterFactory<SlotAction>}
     */
    @NotNull
    private RuntimeTypeAdapterFactory<SlotAction> createSlotActionAdapter() {
        SlotActionManager slotActionManager = this.instance.getSlotActionManager();

        RuntimeTypeAdapterFactory<SlotAction> slotFactory = RuntimeTypeAdapterFactory
                .of(SlotAction.class, "type");

        slotActionManager.getClasses().forEach(clazz -> {
            Optional<Triplet<String, Class<? extends SlotActionInventory>, ItemStack>> optionalContainer = slotActionManager.getContainerByClass(clazz);

            if(optionalContainer.isEmpty()) return;
            Triplet<String, Class<? extends SlotActionInventory>, ItemStack> container = optionalContainer.get();

            slotFactory.registerSubtype(clazz, container.getA());
        });

        return slotFactory;
    }

    /**
     * Create the adapter dynamically from the @link{{@link ItemActionManager}}
     * with class and type name for the json object
     * @return {@link RuntimeTypeAdapterFactory<ItemAction>}
     */
    @NotNull
    private RuntimeTypeAdapterFactory<ItemAction> createItemActionAdapter() {
        ItemActionManager itemActionManager = this.instance.getItemActionManager();

        RuntimeTypeAdapterFactory<ItemAction> itemFactory = RuntimeTypeAdapterFactory
                .of(ItemAction.class, "type");

        itemActionManager.getClasses().forEach(clazz -> {
            Optional<Triplet<String, Class<? extends ItemActionInventory>, ItemStack>> optionalContainer = itemActionManager.getContainerByClass(clazz);

            if(optionalContainer.isEmpty()) return;
            Triplet<String, Class<? extends ItemActionInventory>, ItemStack> container = optionalContainer.get();

            itemFactory.registerSubtype(clazz, container.getA());
        });

        return itemFactory;
    }

    /**
     * Create a Gson object with appended adapter (Slot Action and Item Action)
     * Only variables with annotation @Expose are saved in the json
     * @return {@link Gson} the new version after rebuilding
     */
    @NotNull
    public Gson rebuildGson() {
        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(createSlotActionAdapter())
                .registerTypeAdapterFactory(createItemActionAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
                .setPrettyPrinting()
                .create();

        return gson;
    }

    @NotNull
    public Gson getGson() {
        return gson;
    }
}
