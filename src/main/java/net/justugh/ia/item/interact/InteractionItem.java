package net.justugh.ia.item.interact;

import lombok.Getter;
import net.justugh.ia.ItemActions;
import net.justugh.ia.cooldown.CooldownData;
import net.justugh.ia.cooldown.CooldownUtil;
import net.justugh.ia.item.ActionItem;
import net.justugh.ia.item.ActionItemType;
import net.justugh.ia.item.action.ItemAction;
import net.justugh.ia.item.data.ItemActionData;
import net.justugh.ia.item.data.ItemDataInterface;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class InteractionItem extends ActionItem {

    private final List<Action> interactActions;
    private final String bypassPermission;
    private final int cooldown;
    private final boolean consume;

    public InteractionItem(String id, ActionItemType type, ItemDataInterface data, String permission, List<ItemAction> actions, List<Action> interactActions, String bypassPermission, int cooldown, boolean consume) {
        super(id, type, data, permission, actions);
        this.interactActions = interactActions;
        this.bypassPermission = bypassPermission;
        this.cooldown = cooldown;
        this.consume = consume;
    }

    public void interact(Player player) {
        if (CooldownUtil.hasCooldown(getId()) && !player.hasPermission(bypassPermission)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    ItemActions.getInstance().getConfig().getString("Messages.Cooldown-Message")));
            return;
        }

        executeActions(player);

        if (bypassPermission != null && !player.hasPermission(bypassPermission)) {
            CooldownUtil.startCooldown(new CooldownData(player.getUniqueId(), getId(), null, cooldown));
        }

        if(consume) {
            ItemStack item;

            if(getData().matches(player.getInventory().getItemInMainHand())) {
                item = player.getInventory().getItemInMainHand();

                if(item.getAmount() <= 1) {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                    return;
                }

                item.setAmount(item.getAmount() - 1);
            }

            if(getData().matches(player.getInventory().getItemInOffHand())) {
                item = player.getInventory().getItemInOffHand();

                if(item.getAmount() <= 1) {
                    player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    return;
                }

                item.setAmount(item.getAmount() - 1);
            }
        }
    }

}
