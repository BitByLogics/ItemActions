package net.justugh.ia.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import net.justugh.ia.ItemActions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Getter
public class ItemManager {

    private final ItemActions plugin;
    private final Set<ActionItem> items;

    public ItemManager(ItemActions plugin) {
        this.plugin = plugin;
        items = Sets.newHashSet();
        loadItems(plugin.getConfig());
    }

    public void loadItems(FileConfiguration config) {
        items.clear();

        ConfigurationSection itemsSection = config.getConfigurationSection("Items");
        Preconditions.checkNotNull(itemsSection, "Items Section is null");

        for (String id : itemsSection.getKeys(false)) {
            if (items.stream().anyMatch(item -> item.getId().equalsIgnoreCase(id))) {
                plugin.getLogger().warning("Skipping item '" + id + "', duplicate ID.");
                continue;
            }

            ConfigurationSection itemSection = config.getConfigurationSection("Items." + id);

            if (itemSection == null) {
                plugin.getLogger().severe("Item '" + id + "' doesn't have a valid configuration section, skipping.");
                continue;
            }

            ItemData itemData = new ItemData();

            List<Material> materials = Lists.newArrayList();
            itemSection.getStringList("Item.materials").forEach(materialName -> {
                if (Arrays.stream(Material.values()).noneMatch(material -> material.name().equalsIgnoreCase(materialName))) {
                    return;
                }

                materials.add(Material.valueOf(materialName.toUpperCase()));
            });
            String name = itemSection.getString("Item.name");

            String dataKey = itemSection.getString("Item.data-key");
            String dataType = itemSection.getString("Item.data-type");
            String dataValue = itemSection.getString("Item.data-value");

            ItemNamespaceData namespaceData = new ItemNamespaceData(dataKey, dataType, dataValue);

            itemData.setMaterials(materials);
            itemData.setName(name == null ? null : ChatColor.translateAlternateColorCodes('&', name));
            itemData.setNamespaceData(namespaceData);

            List<Action> actions = Lists.newArrayList();
            String permission = itemSection.getString("Requirements.permission");
            itemSection.getStringList("Requirements.action-types").forEach(actionName -> {
                if (Arrays.stream(Action.values()).noneMatch(action -> action.name().equalsIgnoreCase(actionName))) {
                    plugin.getLogger().warning("Invalid action '" + actionName + "', skipping.");
                    return;
                }

                actions.add(Action.valueOf(actionName.toUpperCase()));
            });

            ItemRequirementData requirementData = new ItemRequirementData(actions, permission);

            String actionSoundName = itemSection.getString("Actions.action-sound");
            List<String> playerCommands = itemSection.getStringList("Actions.player-commands");
            List<String> consoleCommands = itemSection.getStringList("Actions.console-commands");

            Sound actionSound = (actionSoundName == null
                    || Arrays.stream(Sound.values()).noneMatch(sound ->
                    sound.name().equalsIgnoreCase(actionSoundName)))
                    ? null : Sound.valueOf(actionSoundName.toUpperCase());

            ItemActionData actionData = new ItemActionData(actionSound, playerCommands, consoleCommands);

            items.add(new ActionItem(id, itemData, requirementData, actionData, itemSection.getString("Bypass-Permission"), itemSection.getInt("Cooldown", 0)));
        }

        plugin.getLogger().info("Loaded " + items.size() + " action items from the configuration!");
    }

    public ActionItem getItem(String id) {
        return items.stream().filter(item -> item.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    public ActionItem getItem(ItemStack itemStack) {
        return items.stream().filter(item -> item.getData().matches(itemStack)).findFirst().orElse(null);
    }

    public boolean isActionItem(ItemStack itemStack) {
        return items.stream().anyMatch(item -> item.getData().matches(itemStack));
    }

}
