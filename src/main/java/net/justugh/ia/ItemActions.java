package net.justugh.ia;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.Lists;
import lombok.Getter;
import net.justugh.ia.command.ItemActionsCommand;
import net.justugh.ia.item.ItemManager;
import net.justugh.ia.listener.ItemActionListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class ItemActions extends JavaPlugin {

    @Getter
    private static ItemActions instance;

    private PaperCommandManager commandManager;
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        commandManager = new PaperCommandManager(this);
        itemManager = new ItemManager(this);

        loadCommands();

        getServer().getPluginManager().registerEvents(new ItemActionListener(itemManager), this);
    }

    @Override
    public void onDisable() {

    }

    private void loadCommands() {
        commandManager.getCommandCompletions().registerCompletion("ItemIds", context -> {
            List<String> items = Lists.newArrayList();
            itemManager.getItems().forEach(itemId -> items.add(itemId.getId()));
            return items;
        });

        commandManager.registerCommand(new ItemActionsCommand());
    }

}
