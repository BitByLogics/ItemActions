package net.justugh.ia.listener;

import net.justugh.ia.item.ActionItem;
import net.justugh.ia.item.ItemManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemActionListener implements Listener {

    private final ItemManager itemManager;

    public ItemActionListener(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) {
            return;
        }

        if(!itemManager.isActionItem(event.getItem())) {
            return;
        }

        ActionItem actionItem = itemManager.getItem(event.getItem());

        if(!event.getPlayer().hasPermission(actionItem.getRequirements().getPermission())) {
           return;
        }

        if(!actionItem.getRequirements().getActions().contains(event.getAction())) {
            return;
        }

        actionItem.use(event.getPlayer());
    }

}
