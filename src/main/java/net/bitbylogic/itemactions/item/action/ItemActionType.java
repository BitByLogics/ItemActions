package net.bitbylogic.itemactions.item.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bitbylogic.apibylogic.util.StringUtil;
import net.bitbylogic.apibylogic.util.message.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
@AllArgsConstructor
public enum ItemActionType {
    ADD_POTION_EFFECT((player, args) -> {
        PotionEffectType potionEffectType = PotionEffectType.getByName(args[0]);
        int amplifier = Integer.parseInt(args[1]);
        boolean ambient = Boolean.parseBoolean(args[2]);
        boolean particles = Boolean.parseBoolean(args[3]);

        player.addPotionEffect(new PotionEffect(potionEffectType, 100, amplifier, ambient, particles));
    }),
    REMOVE_POTION_EFFECT((player, args) -> {
        PotionEffectType potionEffectType = PotionEffectType.getByName(args[0]);

        player.removePotionEffect(potionEffectType);
    }),
    NEGATE_FALL_DAMAGE((player, args) -> {
    }),
    SEND_MESSAGE((player, args) -> {
        player.sendMessage(Messages.format(StringUtil.join(0, args, " ")));
    }),
    PLAY_SOUND((player, args) -> {
        Sound sound = Sound.valueOf(args[0].toUpperCase());
        player.playSound(player.getLocation(), sound, 1f, 1f);
    }),
    RUN_COMMAND((player, args) -> {
        player.performCommand(StringUtil.join(0, args, " "));
    }),
    RUN_CONSOLE_COMMAND((player, args) -> {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtil.join(0, args, " ").replace("%player%", player.getName()));
    });

    private ItemActionSupplier supplier;
}