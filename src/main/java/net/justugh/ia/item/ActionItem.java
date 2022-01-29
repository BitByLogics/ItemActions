package net.justugh.ia.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.justugh.ia.item.action.ItemAction;
import net.justugh.ia.item.action.ItemActionType;
import net.justugh.ia.item.data.ItemDataInterface;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class ActionItem {

    private final String id;
    private final ActionItemType type;
    private final ItemDataInterface data;
    private final String permission;
    private final List<ItemAction> actions;

    public boolean hasAction(ItemActionType type) {
        return actions.stream().anyMatch(action -> action.getType() == type);
    }

    public void executeActions(Player player) {
        actions.forEach(action -> action.execute(player));
    }

}
