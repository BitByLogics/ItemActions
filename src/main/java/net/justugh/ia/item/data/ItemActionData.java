package net.justugh.ia.item.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Sound;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemActionData {

    private final Sound actionSound;
    private final List<String> playerCommands;
    private final List<String> consoleCommands;

}
