package me.i3ick.winterslash.commands;


import me.i3ick.winterslash.WinterSlashArenaCreator;
import me.i3ick.winterslash.WinterSlashGameController;
import me.i3ick.winterslash.WinterSlashMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    WinterSlashMain plugin;
    Subcommands subcmnds;
    WinterSlashGameController gameController;
    WinterSlashArenaCreator creator;

    public MainCommand(WinterSlashMain passedPlugin, WinterSlashArenaCreator pass1, WinterSlashGameController pass2) {
        subcmnds = new Subcommands(passedPlugin, pass2, pass1);
        this.plugin = passedPlugin;
        this.creator = pass1;
        this.gameController = pass2;
        
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
    
    public static boolean isDouble(String number){
        try{
            Double.parseDouble(number);
            return true;
        }
        catch(NumberFormatException nfe){
            return false;
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        Player player = (Player) sender;
        
        if(!(player instanceof Player)){
            plugin.getLogger().info("Ya fuck of ya stinky console m8 and come ingame like a man ya fukin pussy!");
            plugin.getLogger().info("No, but srsly, you can't controll WinterSlash commands through the console");
            return true;
        }
        
        if(args.length == 0 || args[0].equalsIgnoreCase("help")){
            if(sender.hasPermission("winterslash.*")){
                    subcmnds.helpMod(player);
            }
            else if(sender.hasPermission("winterslashplayers.*")){
                    subcmnds.helpPlayer(player);
            }
            else{
                player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws help");
            }
            return false;
        }
            
        // joining the arena
        if (args[0].equalsIgnoreCase("join")) {
            if(!sender.hasPermission("winterslash.join")){
                sender.sendMessage("No permission!");
                return true;
            }
            if(args.length == 1){
                subcmnds.joinRandom(player);
            }
            if(args.length == 2){
                subcmnds.join(player, args[1]);
            }else{
                player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws join <arenaname>");
                return true;
            }
        }
        
        if (args[0].equalsIgnoreCase("list")) {
            if(!sender.hasPermission("winterslash.list")){
                sender.sendMessage("No permission!");
                return true;
            }
            if ((args.length == 1)) {
                subcmnds.list(player);
                return true;
            }
            player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws list");
            return true;
        }
        
        if(args[0].equalsIgnoreCase("fs")){
            if(!sender.hasPermission("winterslash.forcestart")){
                sender.sendMessage("No permission!");
                return true;
            }
            if(args.length == 2){
                subcmnds.forceStart(player, args[1]);
            }
            
        }
        
        if(args[0].equalsIgnoreCase("end")){
            if(!sender.hasPermission("winterslash.forceend")){
                sender.sendMessage("No permission!");
                return true;
            }
            if(args.length == 2){
                subcmnds.forceEnd(player, args[1]);
            }
            
        }
        
        if(args[0].equalsIgnoreCase("end")){
            if(!sender.hasPermission("winterslash.forceend")){
                sender.sendMessage("No permission!");
                return true;
            }
            if(args.length == 2){
                subcmnds.forceEnd(player, args[1]);
            }
            
        }
        
        if(args[0].equalsIgnoreCase("award")){
            if(!sender.hasPermission("winterslash.award")){
                sender.sendMessage("No permission!");
                return true;
            }
            
            if(args.length == 2){
                if(isDouble(args[1])){
                    Double dub = Double.parseDouble(args[1]);
                    subcmnds.setAward(dub);
                }
            }
        }
        
        if(args[0].equalsIgnoreCase("leave")){
            if(!sender.hasPermission("winterslash.leave")){
                sender.sendMessage("No permission!");
                return true;
            }
            if(args.length == 1){
                subcmnds.leave(player);
            }
            return true;
        }
        
/*        if(args[0].equalsIgnoreCase("pn")){
            if(!sender.hasPermission("winterslash.pn")){
                sender.sendMessage("No permission!");
                return true;
            }
            if(args.length == 2){
                if(isInt(args[2])){
                int num = Integer.parseInt(args[2]);
                subcmnds.maxPlayerNumber(num, player);
                }
                else{
                    player.sendMessage(ChatColor.RED + args[2] + " is not a number!!!");
                    player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws pn <number>");
                    
                }
            }
            return true;
        }
        */
        
        if(args[0].equalsIgnoreCase("remove")){
            if(!sender.hasPermission("winterslash.remove")){
                sender.sendMessage("No permission!");
                return true;
            }
            if(args.length == 2){
                subcmnds.removeArena(args[1], player);
            }
            else{
                player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws remove <arenaname>");
            }
            return true;
        }
        
        if(args[0].equalsIgnoreCase("setred")){
            if(!sender.hasPermission("winterslash.setred")){
                sender.sendMessage("No permission!");
                return true;
            }
            if(args.length == 1){
                subcmnds.setRed(player);
            }
            else{
                player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws setred");
            }
            return true;
        }
        
        if(args[0].equalsIgnoreCase("setgreen")){
            if(!sender.hasPermission("winterslash.setgreen")){
                sender.sendMessage("No permission!");
                return true;
            }
            if(args.length == 1){
                subcmnds.setGreen(player);
            }
            else{
                player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws setgreen");
            }
            return true;
        }
        
        if(args[0].equalsIgnoreCase("setlobby")){
            if(!sender.hasPermission("winterslash.setlobby")){
                sender.sendMessage("No permission!");
                return true;
            }
            if(args.length == 1){
                subcmnds.setLobby(player);
            }
            else{
                player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws setlobby");
            }
            return true;
        }

        if(args[0].equalsIgnoreCase("create")){
            if(!sender.hasPermission("winterslash.create")){
                sender.sendMessage("No permission!");
                return true;
            }
            
            if(args.length == 3){
                if(!isInt(args[2])){
                    player.sendMessage(ChatColor.RED + args[2] + " is not a number!");
                    return true;
                }
                int playerNumber = Integer.parseInt(args[2]);
                if(playerNumber < 4){
                    player.sendMessage(ChatColor.YELLOW + " Minimum number of players is 4!");
                    return true;
                }
                subcmnds.create(args[1], player, playerNumber);
                return true;
            }
            else{
                player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws create <arenaname> <playernumber>");
            }
            
            return true;
        }

        
        /** Forces all the players to join a match
         * 
         */

        if(args[0].equalsIgnoreCase("joinall")){
            if(sender.hasPermission("winterslash.joinall*")){
                if(args.length == 2){
                    String name = args[1].toString();
                    subcmnds.joinAll(player, name);
                }
            }
            else{
                player.sendMessage(ChatColor.YELLOW + "Proper forumalation is: /ws joinall <arenaname>");
            }
            return true;
        }
        
        
       
        if(sender.hasPermission("winterslash.*")){
            if(args.length == 1){
                subcmnds.helpMod(player);
                return true;
            }
        }
        if(sender.hasPermission("winterslashplayers.*")){
            if(args.length == 1){
                subcmnds.helpPlayer(player);
                return true;
            }
        }
        else{
            player.sendMessage("No permission!");
        }
        
  
        return false;
    }
}
