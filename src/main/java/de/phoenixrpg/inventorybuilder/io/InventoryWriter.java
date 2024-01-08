package de.phoenixrpg.inventorybuilder.io;

import com.google.gson.Gson;
import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class InventoryWriter {

    private final InventoryBuilder instance;

    public InventoryWriter(InventoryBuilder instance) {
        this.instance = instance;
    }

    /**
     * Removes the json file of given {@link InteractiveInventory}
     * @param interactiveInventory Inventory that should be removed
     * @return {@code true} when removing was successfully, {@code false} otherwise
     */
    public boolean removeInventory(@NotNull InteractiveInventory interactiveInventory) {
        File file = new File(InventoryBuilder.getInstance().getDataFolder().getAbsolutePath() + "/" + interactiveInventory.getKey() + ".json");

        if(file.exists())
            return file.delete();
        return false;
    }

    /**
     * Creating json file of {@link InteractiveInventory}
     * Removes the json if the file was existing before running the method
     * The path where the file is created is the plugin folder
     * @param inventory that should be converted to json
     * @return {@code true} when the creation was successfully, {@code false} otherwise
     */
    public boolean writeInventory(@NotNull InteractiveInventory inventory) {
        File file = new File(instance.getDataFolder().getAbsolutePath() + "/" + inventory.getKey() + ".json");

        // Only runs when the file exists and deleting wasn't successfully
        // returning false because the FileWriter would append the other object and cause afterward a mess and converting problem
        if(file.exists() && !file.delete()) {
                return  false;
        }

        try {
            if(!file.createNewFile()) return false;
            Writer writer = new FileWriter(file);

            Gson gson = instance.getInventoryIO().getGson();

            gson.toJson(inventory, writer);

            writer.flush();
            writer.close();
        } catch (IOException ignored) {
            return false;
        }
        return true;

    }
}
