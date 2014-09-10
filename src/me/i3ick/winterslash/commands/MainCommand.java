package me.i3ick.winterslash.commands;

import java.io.File;

import me.i3ick.winterslash.WinterSlashMain;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    WinterSlashMain plugin;
    Subcommands subcmnds;

    public MainCommand(WinterSlashMain passedPlugin) {

        subcmnds = new Subcommands(passedPlugin);
        this.plugin = passedPlugin;
    }
    
    public static boolean isInt(String number){
        try{
            Integer.parseInt(number);
            return true;
        }
        catch(NumberFormatException nfe){
            return false;
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
            String[] args) {
        Player player = (Player) sender;

        // joining the arena
        if (args[1].equalsIgnoreCase("join")) {
            if (!(args.length == 2)) {
                player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws join <arenaname>");
            }
            subcmnds.join(player, args[2]);
            return true;
        }
        
        if (args[0].equalsIgnoreCase("list")) {
            if (!(args.length == 1)) {
                player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws list");
            }
            subcmnds.list(player);
            return true;
        }
        
        if(args[1].equalsIgnoreCase("leave")){
            if(args.length == 1){
                subcmnds.leave(player);
            }
        }
        
        if(args[1].equalsIgnoreCase("pn")){
            if(args.length == 2){
                if(isInt(args[2])){
                int num = Integer.parseInt(args[2]);
                subcmnds.playerNumber(num, player);
                }
                else{
                    player.sendMessage(ChatColor.RED + args[2] + " is not a number!!!");
                    player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws pn <number>");
                    
                }
            }
            player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws pn <number>");
        }

        return false;
    }

}
