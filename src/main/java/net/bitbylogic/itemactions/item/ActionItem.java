package net.bitbylogic.itemactions.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bitbylogic.itemactions.item.action.ItemAction;
import net.bitbylogic.itemactions.item.action.ItemActionType;
import net.bitbylogic.itemactions.item.data.ItemData;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@AllArgsConstructor
public abstract class ActionItem {

    private final String id;
    private final ActionItemType type;
    private final ItemData data;
    private final String permission;
    private final List<ItemAction> actions;

    public boolean hasAction(ItemActionType type) {
        return actions.stream().anyMatch(action -> action.getType() == type);
    }

    public void executeActions(Player player) {
        actions.forEach(action -> action.execute(player));
    }

}
