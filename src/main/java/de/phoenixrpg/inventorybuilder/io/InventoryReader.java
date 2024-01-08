package de.phoenixrpg.inventorybuilder.io;

import com.google.gson.Gson;
import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Optional;

public class InventoryReader {

    private final InventoryBuilder instance;

    public InventoryReader(InventoryBuilder instance) {
        this.instance = instance;
    }

    /**
     * Converts json file to {@link InteractiveInventory}
     * @return {@link Optional<InteractiveInventory>} or when converting fails: {@link Optional with empty element}
     */
    @NotNull
    public Optional<InteractiveInventory> readInventory(@NotNull String fileName) {
        File file = new File(fileName);

        if(!file.exists()) return Optional.empty();

        try {
            Reader reader = new FileReader(file);
            Gson gson = instance.getInventoryIO().getGson();

            InteractiveInventory interactiveInventory = gson.fromJson(reader, InteractiveInventory.class);
            return Optional.of(interactiveInventory);
        } catch (IOException ignored) {
            return Optional.empty();
        }
    }
}
