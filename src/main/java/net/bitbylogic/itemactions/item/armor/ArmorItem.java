package net.bitbylogic.itemactions.item.armor;

import lombok.Getter;
import net.bitbylogic.itemactions.item.ActionItem;
import net.bitbylogic.itemactions.item.ActionItemType;
import net.bitbylogic.itemactions.item.action.ItemAction;
import net.bitbylogic.itemactions.item.data.ItemData;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class ArmorItem extends ActionItem {

    private final List<ItemAction> removeActions;

    public ArmorItem(String id, ActionItemType type, ItemData data, String permission, List<ItemAction> actions, List<ItemAction> removeActions) {
        super(id, type, data, permission, actions);
        this.removeActions = removeActions;
    }

    public void executeUnequipActions(Player player) {
        removeActions.forEach(action -> action.execute(player));
    }

}
