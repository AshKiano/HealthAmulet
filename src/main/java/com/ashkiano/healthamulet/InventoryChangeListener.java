package com.ashkiano.healthamulet;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryChangeListener implements Listener {

    private final int resistanceLevel;

    public InventoryChangeListener(int resistanceLevel) {
        this.resistanceLevel = resistanceLevel;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        runDelayedCheck(event.getWhoClicked());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        runDelayedCheck(event.getPlayer());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        runDelayedCheck(event.getWhoClicked());
    }

    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        runDelayedCheck(event.getPlayer());
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        runDelayedCheck(event.getPlayer());
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        runDelayedCheck(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        runDelayedCheck(event.getPlayer());
    }

    private void runDelayedCheck(HumanEntity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                AmuletTask.checkAmulet(entity, resistanceLevel);
            }
        }.runTaskLater(HealthAmulet.getPlugin(), 1L);
    }
}

