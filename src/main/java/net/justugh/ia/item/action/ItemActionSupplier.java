package net.justugh.ia.item.action;

import org.bukkit.entity.Player;

public interface ItemActionSupplier {

    void onExecute(Player player, String[] args);
    
}
