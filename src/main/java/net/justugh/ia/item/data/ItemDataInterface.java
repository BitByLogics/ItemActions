package net.justugh.ia.item.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
public abstract class ItemDataInterface {

    private ItemStack item;

    public abstract boolean matches(ItemStack itemStack);

}
