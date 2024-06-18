package net.bitbylogic.itemactions.item.data;

import lombok.Getter;
import lombok.Setter;
import net.bitbylogic.apibylogic.util.item.ItemStackUtil;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemData {

    private ItemNamespaceData namespaceData;
    private List<ItemStack> items = new ArrayList<>();

    public boolean matches(ItemStack item) {
        for (ItemStack itemStack : items) {
            if (!ItemStackUtil.isSimilar(itemStack, item, true, true, true)) {
                continue;
            }

            if (namespaceData != null && !namespaceData.matches(item.getItemMeta().getPersistentDataContainer())) {
                continue;
            }

            return true;
        }

        return false;
    }

}
