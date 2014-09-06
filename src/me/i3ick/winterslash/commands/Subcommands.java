package me.i3ick.winterslash.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.i3ick.winterslash.WinterSlashMain;

public class Subcommands {

	WinterSlashMain plugin;

	public Subcommands(WinterSlashMain PassPlugin) {
		this.plugin = PassPlugin;
		this.plugin = (WinterSlashMain) WinterSlashMain.getInstance();
	}

	FileConfiguration config = plugin.getConfig();

	// instance the main class

	// Subcommand

	public void wsjoin(Player player, String arenaName) {

		int maxplayers = config.getInt("arenas." + arenaName + ".maxPlayers");
		// checks permission
		if (!player.hasPermission("freezetag.ftj")) {
			player.sendMessage("No permission");
			return;
		}

		if (WinterSlashController.getInstance().getArena(arenaName) == null) {
			player.sendMessage(ChatColor.RED + "This arena doesn't exist");
			return;
		}

		if (((WinterSlashMain) WinterSlashMain.getInstance())
				.isInventoryEmpty(player)) {
			player.sendMessage(ChatColor.YELLOW
					+ "Please empty your inventory!");
			return;
		}

		if (WinterSlashController.getManager().getArena(arenaName).getPlayers()
				.contains(player.getName())) {
			player.sendMessage(ChatColor.YELLOW
					+ "You are already in this arena!");
			return;
		}

		if (WinterSlashController.getInstance().getArena(arenaName).isInGame()) {
			player.sendMessage(ChatColor.YELLOW
					+ "There is a game currently running in this arena!");
			return;
		}

		if (!(args.length == 1)) {
			player.sendMessage(ChatColor.YELLOW
					+ "Proper forumalation is: /ftj <arenaname>");
			return;
		} else {
			player.sendMessage(ChatColor.YELLOW
					+ "You have been put on the games waiting list.");

			// saves player location

			config.set("PlayerData." + player.getName() + ".X", player
					.getLocation().getBlockX());
			config.set("PlayerData." + player.getName() + ".Y", player
					.getLocation().getBlockY());
			config.set("PlayerData." + player.getName() + ".Z", player
					.getLocation().getBlockZ());
			config.set("PlayerData." + player.getName() + ".Yaw", player
					.getLocation().getYaw());
			config.set("PlayerData." + player.getName() + ".Pitch", player
					.getLocation().getPitch());
			config.set("PlayerData." + player.getName() + ".World",
					Bukkit.getName());
			config.set("PlayerData." + player.getName() + ".World",
					Bukkit.getName());
			config.options().copyDefaults(true);
			WinterSlashMain.getInstance().saveConfig();

			// adds the player to the game
			WinterSlashController.getManager().addPlayers(player, arenaName);

			// This starts the game
			if (wsplayersHM.size() >= maxplayers) {

				// initiates arena manager

				ItemStack revivor = new ItemStack(Material.BLAZE_ROD, 1);
				ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);

			}
		}
	}

}
