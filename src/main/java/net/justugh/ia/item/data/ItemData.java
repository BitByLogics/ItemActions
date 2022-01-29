package net.justugh.ia.item.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

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

            if(getDyeColor() != null && item.getType().name().startsWith("LEATHER_")) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) item.getItemMeta();
                java.awt.Color color = ChatColor.of(getDyeColor()).getColor();
                leatherArmorMeta.setColor(Color.fromRGB(color.getRed(), color.getGreen(), color.getBlue()));
                item.setItemMeta(leatherArmorMeta);
            }

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
