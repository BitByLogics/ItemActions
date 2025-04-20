package net.bitbylogic.itemactions.command;

import com.google.common.collect.Lists;
import net.bitbylogic.itemactions.ItemActions;
import net.bitbylogic.itemactions.item.ActionItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class ItemActionsCommand implements CommandExecutor, TabCompleter {

    private final ItemActions plugin;

    public ItemActionsCommand(ItemActions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!sender.hasPermission("itemactions.help")) {
                sender.sendMessage("§cNo permission.");
                return true;
            }

            sender.sendMessage("§aItem Actions Commands:");
            sender.sendMessage("§a/ia reload §8- §eReload the current items and configuration.");
            sender.sendMessage("§a/ia give <player> <id> §8- §eGive a player an action item.");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("itemactions.reload")) {
                sender.sendMessage("§cNo permission.");
                return true;
            }

            plugin.reloadConfig();
            plugin.getItemManager().loadItems(plugin.getConfig());
            sender.sendMessage("§aSuccessfully reloaded the configuration and loaded §e" + plugin.getItemManager().getItems().size() + " §aitems.");
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("itemactions.give")) {
                sender.sendMessage("§cNo permission.");
                return true;
            }

            if (args.length < 3) {
                sender.sendMessage("§cUsage: /ia give <player> <id>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                sender.sendMessage("§cInvalid player.");
                return true;
            }

            ActionItem actionItem = plugin.getItemManager().getItem(args[2]);

            if (actionItem == null) {
                sender.sendMessage("§cInvalid Action Item.");
                return true;
            }

            target.getInventory().addItem(actionItem.getData().getItem());
            sender.sendMessage("§aSuccessfully gave §e" + target.getName() + " §athe §e" + args[2] + " §aAction Item(s)!");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!args[0].equalsIgnoreCase("give")) {
            return null;
        }

        if (args.length == 2) {
            List<String> onlinePlayers = Lists.newArrayList();
            Bukkit.getOnlinePlayers().forEach(player -> onlinePlayers.add(player.getName()));
            return onlinePlayers;
        }

        List<String> items = Lists.newArrayList();
        plugin.getItemManager().getItems().forEach(itemId -> items.add(itemId.getId()));
        return items;
    }

}
