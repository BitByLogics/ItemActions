package net.justugh.ia.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
@Setter
public class ItemData {

    private List<Material> materials;
    private String name;
    private ItemNamespaceData namespaceData;

    public List<ItemStack> getItems() {
        Preconditions.checkNotNull(materials, "Cannot construct item with null materials.");

        List<ItemStack> items = Lists.newArrayList();

        if (materials.isEmpty()) {
            return items;
        }

        materials.forEach(material -> {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            if (name != null) {
                meta.setDisplayName(name);
            }

            namespaceData.apply(meta.getPersistentDataContainer());
            item.setItemMeta(meta);
            items.add(item);
        });

        return items;
    }

    public boolean matches(ItemStack item) {
        if (!materials.isEmpty() && !materials.contains(item.getType())) {
            return false;
        }

        if (name != null && !item.getItemMeta().getDisplayName().equalsIgnoreCase(name)) {
            return false;
        }

        return namespaceData == null || namespaceData.matches(item.getItemMeta().getPersistentDataContainer());
    }

}
