package net.bitbylogic.itemactions.item.data;

import lombok.Getter;
import lombok.Setter;
import net.bitbylogic.utils.item.ItemStackUtil;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class ItemData {

    private ItemNamespaceData namespaceData;
    private ItemStack item;

    public boolean matches(ItemStack item) {
        if (!ItemStackUtil.isSimilar(item, item, true, true, true)) {
            return false;
        }

        return namespaceData == null || namespaceData.matches(item.getItemMeta().getPersistentDataContainer());
    }

}
