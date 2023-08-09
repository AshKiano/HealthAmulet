package com.ashkiano.healthamulet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class HealthAmulet extends JavaPlugin implements Listener {

    private static HealthAmulet instance;

    protected static final String AMULET_LORE = "An amulet granting extra health.";

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("giveamulet").setExecutor(new GiveAmuletCommand());

        instance = this;

        Metrics metrics = new Metrics(this, 19374);

        this.getLogger().info("Thank you for using the HealthAmulet plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://paypal.me/josefvyskocil");

        getServer().getPluginManager().registerEvents(new InventoryChangeListener(5), this);
    }

    private class GiveAmuletCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be run by a player.");
                return false;
            }

            Player player = (Player) sender;
            ItemStack amulet = new ItemStack(Material.RED_TULIP);
            ItemMeta meta = amulet.getItemMeta();
            meta.setLore(Arrays.asList(AMULET_LORE));
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            amulet.setItemMeta(meta);
            amulet.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

            amulet.setItemMeta(meta);

            player.getInventory().addItem(amulet);
            player.sendMessage("You have been given a health amulet!");
            return true;
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock != null && event.getItem() != null) {
                if (clickedBlock.getRelative(BlockFace.UP).getType() == Material.AIR &&
                        event.getItem().getType() == Material.RED_TULIP &&
                        event.getItem().hasItemMeta()) {
                    ItemMeta meta = event.getItem().getItemMeta();
                    if (meta != null && meta.getLore() != null && meta.getLore().contains(AMULET_LORE)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public void onDisable() {
        instance = null;
    }

    public static HealthAmulet getPlugin() {
        return instance;
    }
}