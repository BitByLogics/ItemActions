package net.bitbylogic.itemactions;

import lombok.Getter;
import net.bitbylogic.apibylogic.updatechecker.UpdateCheckSource;
import net.bitbylogic.apibylogic.updatechecker.UpdateChecker;
import net.bitbylogic.apibylogic.updatechecker.UserAgentBuilder;
import net.bitbylogic.apibylogic.util.message.Formatter;
import net.bitbylogic.itemactions.command.ItemActionsCommand;
import net.bitbylogic.itemactions.item.manager.ItemManager;
import net.bitbylogic.itemactions.listener.ItemActionListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class ItemActions extends JavaPlugin {

    @Getter
    private static ItemActions instance;

    private ItemManager itemManager;

    @Override
    public void onEnable() {
        if (!getServer().getPluginManager().isPluginEnabled("APIByLogic")) {
            getServer().getPluginManager().disablePlugin(this);
            getServer().getConsoleSender().sendMessage(
                    ChatColor.translateAlternateColorCodes('&', "&8[&6Item Actions&8] &cItem Actions was disabled! Please download APIByLogic."),
                    ChatColor.translateAlternateColorCodes('&', "&8[&6Item Actions&8] &ahttps://github.com/BitByLogics/APIByLogic/releases/tag/RELEASE")
            );
            return;
        }

        instance = this;
        saveDefaultConfig();

        new UpdateChecker(this, UpdateCheckSource.SPIGOT, "88840")
                .setNotifyRequesters(false)
                .setNotifyOpsOnJoin(false)
                .setUserAgent(UserAgentBuilder.getDefaultUserAgent())
                .checkEveryXHours(12)
                .onSuccess((commandSenders, latestVersion) -> {
                    String messagePrefix = "&8[&6Item Actions&8] ";
                    String currentVersion = getDescription().getVersion();

                    if (currentVersion.equalsIgnoreCase(latestVersion)) {
                        String updateMessage = Formatter.format(messagePrefix + "&aYou are using the latest version of ItemActions!");

                        Bukkit.getConsoleSender().sendMessage(updateMessage);
                        Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(player -> player.sendMessage(updateMessage));
                        return;
                    }

                    List<String> updateMessages = List.of(
                            Formatter.format(messagePrefix + "&cYour version of ItemActions is outdated!"),
                            Formatter.autoFormat(messagePrefix + "&cYou are using %cv%, latest is %lv%!", currentVersion, latestVersion),
                            Formatter.format(messagePrefix + "&cDownload latest here:"),
                            Formatter.format("&6https://www.spigotmc.org/resources/item-actions.88840/")
                    );

                    Bukkit.getConsoleSender().sendMessage(updateMessages.toArray(new String[]{}));
                    Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(player -> player.sendMessage(updateMessages.toArray(new String[]{})));
                })
                .onFail((commandSenders, e) -> {
                }).checkNow();

        new Metrics(this, 22038);

        itemManager = new ItemManager(this);

        ItemActionsCommand command = new ItemActionsCommand(this);

        getCommand("itemactions").setExecutor(command);
        getCommand("itemactions").setTabCompleter(command);

        getServer().getPluginManager().registerEvents(new ItemActionListener(itemManager), this);
    }

    @Override
    public void onDisable() {

    }

}
