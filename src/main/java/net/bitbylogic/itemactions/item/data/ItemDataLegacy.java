package net.bitbylogic.itemactions.item.data;

import net.bitbylogic.apibylogic.util.ItemStackUtil;
import org.bukkit.inventory.ItemStack;

public class ItemDataLegacy extends ItemDataInterface {

    @Override
    public boolean matches(ItemStack item) {
        return ItemStackUtil.isSimilar(item, getItem(), true, true, true);
    }

}
