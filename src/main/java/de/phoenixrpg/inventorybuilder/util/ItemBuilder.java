package de.phoenixrpg.inventorybuilder.util;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;


public class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        this.itemMeta.setDisplayName(name);
        return this;
    }

    public ItemBuilder setDurability(short damage) {
        this.itemStack.setDurability(damage);
        return this;
    }

    public ItemBuilder setRGB(int red, int green, int blue) {
        if(!(itemMeta instanceof LeatherArmorMeta)) return this;

        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
        leatherArmorMeta.setColor(Color.fromBGR(red, green, blue));
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setList(String ...list) {
        this.itemMeta.setLore(Arrays.asList(list));
        return this;
    }

    public ItemBuilder setList(ArrayList<String> list) {
        this.itemMeta.setLore(list);
        return this;
    }

    public ItemBuilder addGlow() {
        this.itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
        this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(itemMeta);
        return this.itemStack;
    }
}
