package net.bitbylogic.itemactions;

import lombok.Getter;
import net.bitbylogic.itemactions.command.ItemActionsCommand;
import net.bitbylogic.itemactions.item.manager.ItemManager;
import net.bitbylogic.itemactions.listener.ItemActionListener;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ItemActions extends JavaPlugin {

    @Getter
    private static ItemActions instance;

    private ItemManager itemManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
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
