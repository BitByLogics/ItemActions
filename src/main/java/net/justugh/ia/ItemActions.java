package net.justugh.ia;

import lombok.Getter;
import net.justugh.ia.item.ItemManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ItemActions extends JavaPlugin {

    private ItemManager itemManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        itemManager = new ItemManager(this);
    }

    @Override
    public void onDisable() {

    }

}
