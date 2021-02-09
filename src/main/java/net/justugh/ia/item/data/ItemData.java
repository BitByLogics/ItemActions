package net.justugh.ia.item.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.justugh.ia.item.ItemNamespaceData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
@Setter
public class ItemData extends ItemDataInterface {

    private ItemNamespaceData namespaceData;

    @Override
    public List<ItemStack> getItems() {
        Preconditions.checkNotNull(getMaterials(), "Cannot construct item with null materials.");

        List<ItemStack> items = Lists.newArrayList();

        if (getMaterials().isEmpty()) {
            return items;
        }

        getMaterials().forEach(material -> {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            if (getName() != null) {
                meta.setDisplayName(getName());
            }

            namespaceData.apply(meta.getPersistentDataContainer());
            item.setItemMeta(meta);
            items.add(item);
        });

        return items;
    }

    @Override
    public boolean matches(ItemStack item) {
        if (!getMaterials().isEmpty() && !getMaterials().contains(item.getType())) {
            return false;
        }

        if (getName() != null && !item.getItemMeta().getDisplayName().equalsIgnoreCase(getName())) {
            return false;
        }

        return namespaceData == null || namespaceData.matches(item.getItemMeta().getPersistentDataContainer());
    }

}
