package de.phoenixrpg.inventorybuilder.commands;

import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.builder.InteractiveInventory;
import de.phoenixrpg.inventorybuilder.inventories.menu.InteractiveOverviewInventory;
import de.phoenixrpg.inventorybuilder.inventories.menu.MainMenuInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryBuilderCommand implements CommandExecutor, TabExecutor {

    private InventoryBuilder instance;
    public InventoryBuilderCommand(InventoryBuilder instance) {
        this.instance = instance;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player player)) return false;

        if(args.length == 0) {
            help(player);
            return false;
        }

        if(args[0].equalsIgnoreCase("create") && args.length > 2) {
            String key = args[1];

            if(instance.getInventoryManager().existInventory(key)) {
                player.sendMessage("§7>§c Inventory with the key§7:§c " + key + " §calready exists§7.");
                return false;
            }

            StringBuilder title = new StringBuilder();
            for(int pos = 2; pos < args.length; pos++) {
                title.append(args[pos]).append(" ");
            }
            title.deleteCharAt(title.length()-1);

            instance.getInventoryManager().addInventory(key, title.toString().replace("&", "§"));
            player.sendMessage("§7>§3 Inventory with the key§7:§b " + key + " §3was created.");
            return true;
        }

        if(args[0].equalsIgnoreCase("title") && args.length > 2) {
            String key = args[1];

            if(!instance.getInventoryManager().existInventory(key)) {
                player.sendMessage("§7>§c Inventory with the key§7:§c " + key + " §cdon't exists§7.");
                return false;
            }

            StringBuilder title = new StringBuilder();
            for(int pos = 2; pos < args.length; pos++) {
                title.append(args[pos]).append(" ");
            }
            title.deleteCharAt(title.length()-1);

            String finishedTitle = title.toString().replace("&", "§");

            Optional<InteractiveInventory> interactiveInventory = instance.getInventoryManager().getInventoryByKey(key);
            interactiveInventory.get().setTitle(finishedTitle);
            player.sendMessage("§7>§3 Set title from Inventory with the key§7:§b " + key + " §3to§7: " + finishedTitle);
            return true;
        }

        switch (args.length) {
            case 1 -> {
                switch (args[0].toLowerCase()) {
                    case "list" -> {
                        Inventory listInventory = new InteractiveOverviewInventory().buildInventory();
                        player.openInventory(listInventory);
                    }
                    default -> help(player);
                }
            }
            case 2 -> {
                switch (args[0].toLowerCase()) {
                    case "editor" -> {
                        String key = args[1];
                        Optional<InteractiveInventory> optionalInventory = instance.getInventoryManager().getInventoryByKey(key);
                        if(optionalInventory.isEmpty()) {
                            player.sendMessage("§7>§c Inventory with the key§7:§c " + key + " §cwas not found§7.");
                            return false;
                        }

                        Inventory menuInventory = new MainMenuInventory(optionalInventory.get()).buildInventory();
                        player.openInventory(menuInventory);
                    }
                    case "open" -> {
                        String key = args[1];
                        Optional<InteractiveInventory> optionalInventory = instance.getInventoryManager().getInventoryByKey(key);
                        if(optionalInventory.isEmpty()) {
                            player.sendMessage("§7>§c Inventory with the key§7:§c " + key + " §cwas not found§7.");
                            return false;
                        }

                        Inventory interactiveInventory = optionalInventory.get().buildInventory();
                        player.openInventory(interactiveInventory);
                    }
                    default -> help(player);
                }
            }
            default -> help(player);
        }

        return false;
    }

    public void help(Player player) {
        player.sendMessage("§7> §3/ib create §b<Key> <Inventory Name> §7| §bCreate interactive inventory");
        player.sendMessage("§7> §3/ib editor §b<Key> §7| §bEdit interactive inventory (edit items and actions) ");
        player.sendMessage("§7> §3/ib open §b<Key> §7| §bOpen inventory to interact with");
        player.sendMessage("§7> §3/ib title §b<Key> <Title> §7| §bSet new inventory title");
        player.sendMessage("§7> §3/ib list §7| §bOpen overview of all available interactive inventories");
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> result = new ArrayList<>();
        if(args.length == 1) return instance.getInventoryManager().getKeys();

        return result;
    }
}
