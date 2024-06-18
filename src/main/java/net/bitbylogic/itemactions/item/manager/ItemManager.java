package net.bitbylogic.itemactions.item.manager;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import net.bitbylogic.apibylogic.util.RichTextUtil;
import net.bitbylogic.apibylogic.util.item.ItemStackUtil;
import net.bitbylogic.itemactions.ItemActions;
import net.bitbylogic.itemactions.item.ActionItem;
import net.bitbylogic.itemactions.item.ActionItemType;
import net.bitbylogic.itemactions.item.action.ItemAction;
import net.bitbylogic.itemactions.item.action.ItemActionType;
import net.bitbylogic.itemactions.item.armor.ArmorItem;
import net.bitbylogic.itemactions.item.data.ItemData;
import net.bitbylogic.itemactions.item.data.ItemNamespaceData;
import net.bitbylogic.itemactions.item.interact.InteractionItem;
import org.bukkit.Material;
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

            String dataKey = itemSection.getString("Item.data-key");
            String dataType = itemSection.getString("Item.data-type");
            String dataValue = itemSection.getString("Item.data-value");

            ItemNamespaceData namespaceData = new ItemNamespaceData(dataKey, dataType, dataValue);

            itemSection.getStringList("Item.materials").forEach(material -> {
                ItemStack item = ItemStackUtil.getItemStackFromConfig(itemSection.getConfigurationSection("Item"));
                item.setType(Material.valueOf(material));
                item.setItemMeta(namespaceData.apply(item.getItemMeta()));
                itemData.getItems().add(item);
            });

            itemData.setNamespaceData(namespaceData);

            ActionItemType itemType = ActionItemType.valueOf(itemSection.getString("Type", "INTERACT"));

            String permission = itemSection.getString("Requirements.permission");
            List<ItemAction> actions = Lists.newArrayList();
            itemSection.getStringList("Actions").forEach(action -> {
                String[] data = action.split(":");
                actions.add(new ItemAction(ItemActionType.valueOf(data[0]), data.length > 1 ? RichTextUtil.getRichText(data, 1) : new String[]{}));
            });

            switch (itemType) {
                case ARMOR:
                    List<ItemAction> uneqipActions = Lists.newArrayList();
                    itemSection.getStringList("Unequip-Actions").forEach(action -> {
                        String[] data = action.split(":");
                        uneqipActions.add(new ItemAction(ItemActionType.valueOf(data[0]), data.length > 1 ? RichTextUtil.getRichText(data, 1) : new String[]{}));
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
