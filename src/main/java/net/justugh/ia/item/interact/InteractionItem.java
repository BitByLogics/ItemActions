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
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.util.List;

@Getter
public class InteractionItem extends ActionItem {

    private final List<Action> interactActions;
    private final String bypassPermission;
    private final int cooldown;

    public InteractionItem(String id, ActionItemType type, ItemDataInterface data, String permission, List<ItemAction> actions, List<Action> interactActions, String bypassPermission, int cooldown) {
        super(id, type, data, permission, actions);
        this.interactActions = interactActions;
        this.bypassPermission = bypassPermission;
        this.cooldown = cooldown;
    }

    public void interact(Player player) {
        if (CooldownUtil.hasCooldown(getId()) && !player.hasPermission(bypassPermission)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    ItemActions.getInstance().getConfig().getString("Messages.Cooldown-Message")));
            return;
        }

        executeActions(player);

        if (!player.hasPermission(bypassPermission)) {
            CooldownUtil.startCooldown(new CooldownData(player.getUniqueId(), getId(), null, cooldown));
        }
    }

}
