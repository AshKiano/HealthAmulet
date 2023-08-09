package com.ashkiano.healthamulet;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class AmuletTask {

    public static void checkAmulet(HumanEntity entity, Integer resistanceLevel) {
        if (!(entity instanceof Player)) return;

        Player player = (Player) entity;

        for (int i = 0; i < 9; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item == null) continue;

            ItemMeta meta = item.getItemMeta();
            if (meta == null) continue;

            List<String> lore = meta.getLore();
            if (lore == null) continue;

            String amuletLore = HealthAmulet.AMULET_LORE;
            if (lore.contains(amuletLore)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, resistanceLevel-1, false, false, false));
                return;
            }
        }

        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
    }
}
