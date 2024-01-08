package de.phoenixrpg.inventorybuilder;

import de.phoenixrpg.inventorybuilder.builder.InventoryManager;
import de.phoenixrpg.inventorybuilder.commands.InventoryBuilderCommand;
import de.phoenixrpg.inventorybuilder.builder.items.ItemActionManager;
import de.phoenixrpg.inventorybuilder.builder.slot.SlotActionManager;
import de.phoenixrpg.inventorybuilder.io.InventoryIO;
import de.phoenixrpg.inventorybuilder.io.InventoryReader;
import de.phoenixrpg.inventorybuilder.io.InventoryWriter;
import de.phoenixrpg.inventorybuilder.listener.InventoryClickListener;
import de.phoenixrpg.inventorybuilder.listener.InventoryCloseListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class InventoryBuilder extends JavaPlugin {

    private static InventoryBuilder INSTANCE;

    private InventoryManager inventoryManager;
    private SlotActionManager slotActionManager;
    private ItemActionManager itemActionManager;

    private InventoryIO inventoryIO;
    private InventoryReader inventoryReader;
    private InventoryWriter inventoryWriter;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.inventoryIO = new InventoryIO(this);
        this.inventoryReader = new InventoryReader(this);
        this.inventoryWriter = new InventoryWriter(this);

        this.slotActionManager = new SlotActionManager(this);
        this.itemActionManager = new ItemActionManager(this);
        this.inventoryManager = new InventoryManager(this);


        this.slotActionManager.loadSlotActions();
        this.itemActionManager.loadItemActions();

        this.inventoryIO.rebuildGson();

        this.inventoryManager.loadInventories();

        registerCommand();
        registerListener();
    }

    public ItemActionManager getItemActionManager() {
        return itemActionManager;
    }

    public SlotActionManager getSlotActionManager() {
        return slotActionManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public InventoryIO getInventoryIO() {
        return inventoryIO;
    }

    public InventoryReader getInventoryReader() {
        return inventoryReader;
    }

    public InventoryWriter getInventoryWriter() {
        return inventoryWriter;
    }

    public void registerCommand() {
       PluginCommand command =  getCommand("inventorybuilder");
       InventoryBuilderCommand contentBuilderCommand = new InventoryBuilderCommand(this);
       command.setExecutor(contentBuilderCommand);
       command.setTabCompleter(contentBuilderCommand);
    }

    public void registerListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new InventoryClickListener(), this);
        pluginManager.registerEvents(new InventoryCloseListener(), this);
    }


    public static InventoryBuilder getInstance() {
        return INSTANCE;
    }
}
