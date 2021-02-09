package net.justugh.ia.item;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
@Setter
public class ItemData {

    private Material material;
    private String name;
    private ItemNamespaceData namespaceData;

    public ItemStack getItem() {
        Preconditions.checkNotNull(material, "Cannot construct item with null material.");

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (name != null) {
            meta.setDisplayName(name);
        }

        namespaceData.apply(meta.getPersistentDataContainer());
        item.setItemMeta(meta);

        return item;
    }

    public boolean matches(ItemStack item) {
        if (material != null && item.getType() != material) {
            return false;
        }

        if (name != null && !item.getItemMeta().getDisplayName().equalsIgnoreCase(name)) {
            return false;
        }

        return namespaceData == null || namespaceData.matches(item.getItemMeta().getPersistentDataContainer());
    }

}
