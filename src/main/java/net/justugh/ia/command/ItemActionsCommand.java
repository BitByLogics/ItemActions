package net.justugh.ia.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.justugh.ia.ItemActions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("itemactions|ia")
public class ItemActionsCommand extends BaseCommand {

    @Dependency
    private ItemActions plugin;

    @Default
    @CommandPermission("itemactions.help")
    public void onDefault(CommandSender commandSender) {
        commandSender.sendMessage("§aItem Actions Commands:");
        commandSender.sendMessage("§a/ia reload §8- §eReload the current items and configuration.");
        commandSender.sendMessage("§a/ia give <player> <id> §8- §eGive a player an action item.");
    }

    @Subcommand("reload")
    @CommandPermission("itemactions.reload")
    public void onReload(CommandSender sender) {
        plugin.reloadConfig();
        plugin.getItemManager().loadItems(plugin.getConfig());
        sender.sendMessage("§aSuccessfully reloaded the configuration and loaded §e" + plugin.getItemManager().getItems().size() + " §aitems.");
    }

    @Subcommand("give")
    @CommandPermission("itemactions.give")
    @Syntax("<player> <id>")
    @CommandCompletion("@Players @ItemIds")
    public void onGive(CommandSender sender, String player, String id) {
        Player target = Bukkit.getPlayer(player);

        if (target == null) {
            sender.sendMessage("§cInvalid player.");
            return;
        }

        if (plugin.getItemManager().getItem(id) == null) {
            sender.sendMessage("§cInvalid Action Item.");
            return;
        }

        plugin.getItemManager().getItem(id).getData().getItems().forEach(item -> target.getInventory().addItem(item));
        sender.sendMessage("§aSuccessfully gave §e" + target.getName() + " §athe §e" + id + " §aAction Item(s)!");
    }

}
