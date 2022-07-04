package net.justugh.ia.item.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import net.justugh.japi.util.ItemStackUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemDataLegacy extends ItemDataInterface {

    @Override
    public boolean matches(ItemStack item) {
        return ItemStackUtil.isSimilar(item, getItem(), true, true, true);
    }

}
