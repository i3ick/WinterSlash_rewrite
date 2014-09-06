package me.i3ick.winterslash.commands;

import me.i3ick.winterslash.WinterSlashMain;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

	WinterSlashMain plugin;
	Subcommands subcmnds;

	public MainCommand(WinterSlashMain passedPlugin) {

		subcmnds = new Subcommands(passedPlugin);
		this.plugin = passedPlugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = (Player) sender;

		// joining the arena
		if (args[0].equalsIgnoreCase("join")) {
			if (args.length < 1) {
				player.sendMessage(ChatColor.RED
						+ "You didn't specify arena name");
				return true;
			}
			subcmnds.wsjoin(player, args[1]);
		}

		return false;
	}

}
