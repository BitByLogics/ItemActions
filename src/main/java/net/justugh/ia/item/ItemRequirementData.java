package net.justugh.ia.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.block.Action;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemRequirementData {

    private final List<Action> actions;
    private final String permission;

}
