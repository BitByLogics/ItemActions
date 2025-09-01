package net.bitbylogic.itemactions;

import com.jeff_media.armorequipevent.ArmorEquipEvent;
import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.jeff_media.updatechecker.UserAgentBuilder;
import lombok.Getter;
import net.bitbylogic.itemactions.command.ItemActionsCommand;
import net.bitbylogic.itemactions.item.manager.ItemManager;
import net.bitbylogic.itemactions.listener.ItemActionListener;
import net.bitbylogic.utils.message.BitColor;
import net.bitbylogic.utils.message.format.Formatter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

@Getter
public class ItemActions extends JavaPlugin {

    @Getter
    private static ItemActions instance;

    private ItemManager itemManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        ArmorEquipEvent.registerListener(this);

        Formatter.registerConfig(new File(getDataFolder(), "config.yml"));
        BitColor.loadColors(getConfig());

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
                .onFail((commandSenders, e) -> {}).checkNow();

        new Metrics(this, 22038);

        itemManager = new ItemManager(this);

        ItemActionsCommand command = new ItemActionsCommand(this);

        getCommand("itemactions").setExecutor(command);
        getCommand("itemactions").setTabCompleter(command);

        getServer().getPluginManager().registerEvents(new ItemActionListener(itemManager), this);
    }

    @Override
    public void onDisable() {
        itemManager.getRecipes().forEach(Bukkit::removeRecipe);
    }

}
