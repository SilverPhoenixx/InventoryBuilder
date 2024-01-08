package de.phoenixrpg.inventorybuilder.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class ItemStackAdapter extends TypeAdapter<ItemStack> {

    /**
     * Converting the ItemStack to String
     * the Bukkit ItemStack gets convert to Minecraft ItemStack
     * with copied NMS
     * afterward the NBT data gets converted to String
     */
    @Override
    public void write(JsonWriter writer, ItemStack value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(value);
        NBTTagCompound nbt = new NBTTagCompound();
        nmsItem.b(nbt);
        writer.value(nbt.toString());
    }

    /**
     * Reading the String and converting to NBT data
     * create a Minecraft ItemStack from given NBT data
     * and afterward create a Bukkit ItemStack by copying the Minecraft ItemStack
     */
    @Override
    public ItemStack read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String tag = reader.nextString();
        NBTTagCompound nbt;
        try {
            nbt = MojangsonParser.a(tag);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        net.minecraft.world.item.ItemStack nbtItem = net.minecraft.world.item.ItemStack.a(nbt);
        return CraftItemStack.asBukkitCopy(nbtItem);
    }

}