package net.bitbylogic.itemactions.item.data;

import lombok.Getter;
import lombok.Setter;
import net.bitbylogic.apibylogic.util.ItemStackUtil;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class ItemData extends ItemDataInterface {

    private ItemNamespaceData namespaceData;

    @Override
    public boolean matches(ItemStack item) {
        if(!ItemStackUtil.isSimilar(item, getItem(), true, true, true)) {
            return false;
        }

        return namespaceData == null || namespaceData.matches(item.getItemMeta().getPersistentDataContainer());
    }

}
