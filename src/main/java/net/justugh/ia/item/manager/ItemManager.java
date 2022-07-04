package net.justugh.ia.item.manager;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import net.justugh.ia.ItemActions;
import net.justugh.ia.item.ActionItem;
import net.justugh.ia.item.ActionItemType;
import net.justugh.ia.item.action.ItemAction;
import net.justugh.ia.item.action.ItemActionType;
import net.justugh.ia.item.armor.ArmorItem;
import net.justugh.ia.item.data.*;
import net.justugh.ia.item.interact.InteractionItem;
import net.justugh.japi.util.Format;
import net.justugh.japi.util.ItemStackUtil;
import net.justugh.japi.util.RichTextUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
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

            ItemDataInterface itemData = new ItemDataLegacy();

            itemData.setItem(ItemStackUtil.getItemStackFromConfig(itemSection.getConfigurationSection("Item")));

            // Temporary Fix, will improve later
            try {
                Class.forName("org.bukkit.persistence.PersistentDataContainer");

                itemData = new ItemData();

                String dataKey = itemSection.getString("Item.data-key");
                String dataType = itemSection.getString("Item.data-type");
                String dataValue = itemSection.getString("Item.data-value");

                ItemNamespaceData namespaceData = new ItemNamespaceData(dataKey, dataType, dataValue);
                itemData.setItem(ItemStackUtil.getItemStackFromConfig(itemSection.getConfigurationSection("Item")));
                itemData.getItem().setItemMeta(namespaceData.apply(itemData.getItem().getItemMeta()));
                ((ItemData) itemData).setNamespaceData(namespaceData);
            } catch (Exception e) {
                plugin.getLogger().warning("Server doesn't support Persistent Data, data-key will not work.");
            }

            ActionItemType itemType = ActionItemType.valueOf(itemSection.getString("Type", "INTERACT"));

            String permission = itemSection.getString("Requirements.permission");
            List<ItemAction> actions = Lists.newArrayList();
            itemSection.getStringList("Actions").forEach(action -> {
                String[] data = action.split(":");
                actions.add(new ItemAction(ItemActionType.valueOf(data[0]), data.length > 1 ? RichTextUtil.getRichText(data, 1) : new String[] {}));
            });

            switch (itemType) {
                case ARMOR:
                    List<ItemAction> uneqipActions = Lists.newArrayList();
                    itemSection.getStringList("Unequip-Actions").forEach(action -> {
                        String[] data = action.split(":");
                        uneqipActions.add(new ItemAction(ItemActionType.valueOf(data[0]), data.length > 1 ? RichTextUtil.getRichText(data, 1) : new String[] {}));
                    });
                    items.add(new ArmorItem(id, itemType, itemData, permission, actions, uneqipActions));
                    continue;
                default:
                case INTERACT:
                    List<Action> interactActions = Lists.newArrayList();
                    itemSection.getStringList("Requirements.action-types").forEach(actionName -> {
                        if (Arrays.stream(Action.values()).noneMatch(action -> action.name().equalsIgnoreCase(actionName))) {
                            plugin.getLogger().warning("Invalid action '" + actionName + "', skipping.");
                            return;
                        }

                        interactActions.add(Action.valueOf(actionName.toUpperCase()));
                    });
                    items.add(new InteractionItem(id, itemType, itemData, permission, actions, interactActions, itemSection.getString("Bypass-Permission"), itemSection.getInt("Cooldown", 0), itemSection.getBoolean("Consume", false)));
                    break;
            }
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
        return itemStack != null && items.stream().anyMatch(item -> item.getData().matches(itemStack));
    }

}
