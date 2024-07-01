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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

        this.getLogger().info("Thank you for using the HealthAmulet plugin! If you enjoy using this plugin, please consider making a donation to support the development. You can donate at: https://donate.ashkiano.com");

        getServer().getPluginManager().registerEvents(new InventoryChangeListener(5), this);

        checkForUpdates();
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

    private void checkForUpdates() {
        try {
            String pluginName = this.getDescription().getName();
            URL url = new URL("https://plugins.ashkiano.com/version_check.php?plugin=" + pluginName);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                if (jsonObject.has("error")) {
                    this.getLogger().warning("Error when checking for updates: " + jsonObject.getString("error"));
                } else {
                    String latestVersion = jsonObject.getString("latest_version");

                    String currentVersion = this.getDescription().getVersion();
                    if (currentVersion.equals(latestVersion)) {
                        this.getLogger().info("This plugin is up to date!");
                    } else {
                        this.getLogger().warning("There is a newer version (" + latestVersion + ") available! Please update!");
                    }
                }
            } else {
                this.getLogger().warning("Failed to check for updates. Response code: " + responseCode);
            }
        } catch (Exception e) {
            this.getLogger().warning("Failed to check for updates. Error: " + e.getMessage());
        }
    }
}