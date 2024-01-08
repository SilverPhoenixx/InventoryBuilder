package de.phoenixrpg.inventorybuilder.builder;

import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.io.InventoryReader;
import de.phoenixrpg.inventorybuilder.io.InventoryWriter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class InventoryManager {

    private final InventoryBuilder instance;
    private final HashMap<String, InteractiveInventory> inventories;

    public InventoryManager(InventoryBuilder instance) {
        this.instance = instance;
        this.inventories = new HashMap<>();
    }

    /**
     * Get the amount of the inventories
     *
     * @return {@code int}
     */
    public int getAmountOfInventories() {
        return this.inventories.size();
    }

    public List<String> getKeys() {
        return inventories.keySet().stream().toList();
    }

    /**
     * Loading all available inventories in plugin folder
     */
    public void loadInventories() {
        if (!InventoryBuilder.getInstance().getDataFolder().exists())
            InventoryBuilder.getInstance().getDataFolder().mkdirs();

        File[] files = InventoryBuilder.getInstance().getDataFolder().listFiles();
        if (files == null) return;

        InventoryReader reader = instance.getInventoryReader();
        for (File file : files) {
            Optional<InteractiveInventory> optionalInventory = reader.readInventory(file.getAbsolutePath());

            optionalInventory
                    .ifPresent(
                            interactiveInventory -> this.inventories.put(interactiveInventory.getKey(), interactiveInventory)
                    );
        }
    }

    /**
     * Check if the inventory exists by given key
     *
     * @param key name of the inventory (not the title)
     * @return {@code true} if the inventory exists, otherwise {@code false}
     */
    public boolean existInventory(@NotNull String key) {
        return inventories.containsKey(key);
    }

    /**
     * Add an inventory to the hashmap and create a json
     *
     * @param key   of the inventory
     * @param title of the given inventory
     * @return {@code true} if the inventory was successfully created, otherwise {@code false}
     */
    public boolean addInventory(@NotNull String key, @NotNull String title) {
        if (existInventory(key)) return false;

        InteractiveInventory interactiveInventory = new InteractiveInventory(key, title);
        interactiveInventory.save();

        this.inventories.put(key, interactiveInventory);
        return true;
    }

    /**
     * Remove an inventory by given key (not the title)
     *
     * @param key of the inventory to be removed
     * @return {@code true} if the inventory was successfully removed, otherwise {@code false}
     */
    public boolean removeInventory(@NotNull String key) {
        if (!existInventory(key)) return false;

        Optional<InteractiveInventory> interactiveInventory = getInventoryByKey(key);
        if (interactiveInventory.isEmpty()) return false;

        this.inventories.remove(key);

        InventoryWriter writer = instance.getInventoryWriter();
        return writer.removeInventory(interactiveInventory.get());
    }

    /**
     * Get the inventory by given key
     * @param key of the searched inventory
     * @return {@link Optional<InteractiveInventory>}
     */
    @NotNull
    public Optional<InteractiveInventory> getInventoryByKey(@NotNull String key) {
        if (!existInventory(key)) return Optional.empty();
        return Optional.of(this.inventories.get(key));
    }

    /**
     * Get the inventory by given key
     * @param index of the searched inventory
     * @return {@link Optional<InteractiveInventory>}
     */
    public Optional<InteractiveInventory> getInventoryByIndex(int index) {
        return Optional.of(this.inventories.values().stream().toList().get(index));
    }

}
