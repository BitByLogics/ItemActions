package net.bitbylogic.itemactions.item.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class ItemAction {

    private final ItemActionType type;
    private final String[] args;

    public void execute(Player player) {
        type.getSupplier().onExecute(player, args);
    }

}
