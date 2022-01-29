package net.justugh.ia.listener;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.justugh.ia.ItemActions;
import net.justugh.ia.item.ActionItem;
import net.justugh.ia.item.ActionItemType;
import net.justugh.ia.item.action.ItemActionType;
import net.justugh.ia.item.armor.ArmorItem;
import net.justugh.ia.item.interact.InteractionItem;
import net.justugh.ia.item.manager.ItemManager;
import net.justugh.japi.util.event.armor.ArmorEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ItemActionListener implements Listener {

    private final ItemManager itemManager;

    private final HashMap<UUID, List<ActionItem>> equippedArmor = Maps.newHashMap();

    public ItemActionListener(ItemManager itemManager) {
        this.itemManager = itemManager;

        Bukkit.getScheduler().runTaskTimer(ItemActions.getInstance(), () -> {
            for (UUID uuid : equippedArmor.keySet()) {
                Player player = Bukkit.getPlayer(uuid);

                if (player == null) {
                    continue;
                }

                List<ActionItem> items = equippedArmor.get(uuid);

                items.stream().filter(item -> item.getActions().stream().anyMatch(action -> action.getType() == ItemActionType.ADD_POTION_EFFECT)).forEach(item -> {
                    item.getActions().stream().filter(action -> action.getType() == ItemActionType.ADD_POTION_EFFECT).forEach(action -> action.execute(player));
                });
            }
        }, 0, 50);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }

        if (!itemManager.isActionItem(event.getItem())) {
            return;
        }

        ActionItem actionItem = itemManager.getItem(event.getItem());

        if (actionItem.getType() != ActionItemType.INTERACT) {
            return;
        }

        InteractionItem interactionItem = (InteractionItem) actionItem;

        if (interactionItem.getPermission() != null && !event.getPlayer().hasPermission(interactionItem.getPermission())) {
            return;
        }

        if (!interactionItem.getInteractActions().isEmpty() && !interactionItem.getInteractActions().contains(event.getAction())) {
            return;
        }

        actionItem.executeActions(event.getPlayer());
    }

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        if (event.getNewArmorPiece() == null) {
            return;
        }

        if (!itemManager.isActionItem(event.getNewArmorPiece())) {
            return;
        }

        ActionItem actionItem = itemManager.getItem(event.getNewArmorPiece());

        if (actionItem.getType() != ActionItemType.ARMOR) {
            return;
        }

        ArmorItem armorItem = (ArmorItem) actionItem;

        armorItem.executeActions(event.getPlayer());
        List<ActionItem> items = equippedArmor.getOrDefault(event.getPlayer().getUniqueId(), Lists.newArrayList());
        items.add(armorItem);
        equippedArmor.put(event.getPlayer().getUniqueId(), items);
    }

    @EventHandler
    public void onArmorUnequip(ArmorEquipEvent event) {
        if (event.getOldArmorPiece() == null) {
            return;
        }

        if (!itemManager.isActionItem(event.getOldArmorPiece())) {
            return;
        }

        ActionItem actionItem = itemManager.getItem(event.getOldArmorPiece());

        if (actionItem.getType() != ActionItemType.ARMOR) {
            return;
        }

        ArmorItem armorItem = (ArmorItem) actionItem;

        armorItem.executeUnequipActions(event.getPlayer());
        List<ActionItem> items = equippedArmor.getOrDefault(event.getPlayer().getUniqueId(), Lists.newArrayList());
        items.remove(armorItem);
        equippedArmor.put(event.getPlayer().getUniqueId(), items);
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER || event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        for (ItemStack armorContent : ((Player) event.getEntity()).getInventory().getArmorContents()) {
            if (armorContent == null) {
                continue;
            }

            if (!itemManager.isActionItem(armorContent)) {
                continue;
            }

            ActionItem actionItem = itemManager.getItem(armorContent);

            if (actionItem.getType() != ActionItemType.ARMOR || !actionItem.hasAction(ItemActionType.NEGATE_FALL_DAMAGE)) {
                continue;
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for (ItemStack item : event.getPlayer().getInventory().getArmorContents()) {
            if (!itemManager.isActionItem(item)) {
                continue;
            }

            ActionItem actionItem = itemManager.getItem(item);

            if (actionItem.getType() != ActionItemType.ARMOR) {
                continue;
            }

            actionItem.getActions().stream().filter(action -> action.getType() == ItemActionType.ADD_POTION_EFFECT).forEach(action -> action.execute(event.getPlayer()));
            List<ActionItem> items = equippedArmor.getOrDefault(event.getPlayer().getUniqueId(), Lists.newArrayList());
            items.add(actionItem);
            equippedArmor.put(event.getPlayer().getUniqueId(), items);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        equippedArmor.remove(event.getPlayer().getUniqueId());
    }

}
