package net.justugh.ia.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.justugh.ia.ItemActions;
import net.justugh.ia.cooldown.CooldownData;
import net.justugh.ia.cooldown.CooldownUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class ActionItem {

    private final String id;
    private final ItemData data;
    private final ItemRequirementData requirements;
    private final ItemActionData actionData;
    private final String bypassPermission;
    private final int cooldown;

    public void use(Player player) {
        if (CooldownUtil.hasCooldown(id) && !player.hasPermission(bypassPermission)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    ItemActions.getInstance().getConfig().getString("Messages.Cooldown-Message")));
            return;
        }

        if (actionData.getActionSound() != null) {
            player.playSound(player.getLocation(), actionData.getActionSound(), 1f, 1f);
        }

        actionData.getPlayerCommands().forEach(player::performCommand);
        actionData.getConsoleCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));

        if (!player.hasPermission(bypassPermission)) {
            CooldownUtil.startCooldown(new CooldownData(player.getUniqueId(), id, null, cooldown));
        }
    }

}
