package net.justugh.ia.item.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.justugh.japi.util.Format;
import net.justugh.japi.util.ItemStackUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

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
