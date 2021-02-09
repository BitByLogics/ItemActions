package net.justugh.ia.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActionItem {

    private final String id;
    private final ItemData data;
    private final ItemRequirementData requirements;
    private final ItemActionData actionData;
    private final int cooldown;


}
