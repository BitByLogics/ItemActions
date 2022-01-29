package net.justugh.ia.item.armor;

import lombok.Getter;
import net.justugh.ia.item.ActionItem;
import net.justugh.ia.item.ActionItemType;
import net.justugh.ia.item.action.ItemAction;
import net.justugh.ia.item.data.ItemDataInterface;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class ArmorItem extends ActionItem {

    private final List<ItemAction> removeActions;

    public ArmorItem(String id, ActionItemType type, ItemDataInterface data, String permission, List<ItemAction> actions, List<ItemAction> removeActions) {
        super(id, type, data, permission, actions);
        this.removeActions = removeActions;
    }

    public void executeUnequipActions(Player player) {
        removeActions.forEach(action -> action.execute(player));
    }

}
