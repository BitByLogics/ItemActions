package net.justugh.ia.cooldown;

import net.justugh.ia.ItemActions;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class CooldownUtil {

    private final static List<CooldownData> cooldownList = new ArrayList<>();

    public static void startCooldown(CooldownData data) {
        cooldownList.add(data);

        if (data.getLength() == 0) {
            return;
        }

        data.setTaskID(Bukkit.getScheduler().runTaskLater(ItemActions.getInstance(), () -> {
            cooldownList.remove(data);

            if (data.getFinishTask() != null) {
                data.getFinishTask().complete(null);
            }
        }, data.getLength() * 20).getTaskId());
    }

    public static boolean hasCooldown(String key) {
        return cooldownList.stream().anyMatch(data -> data.getKey().equalsIgnoreCase(key));
    }

    public static CooldownData getCooldown(String key) {
        return cooldownList.stream().filter(data -> data.getKey().equalsIgnoreCase(key)).findFirst().orElse(null);
    }

}
