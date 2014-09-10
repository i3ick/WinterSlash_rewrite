package me.i3ick.winterslash.commands;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.i3ick.winterslash.WinterSlashArena;
import me.i3ick.winterslash.WinterSlashGameController;
import me.i3ick.winterslash.WinterSlashMain;

public class Subcommands {

    WinterSlashMain plugin;
    WinterSlashGameController plugin2;

    public Subcommands(WinterSlashMain passPlugin) {
        this.plugin = passPlugin;
    }
    
    public Subcommands(WinterSlashGameController passPlugin){
        this.plugin2 = passPlugin;
    }
    

    FileConfiguration config = plugin.getConfig();
    FileConfiguration arenaData = plugin.getArenaData();

   

  

    
    
    /**
     * This method runs checks on the arena status and
     * adds the player to the game lobby.
     * @param player
     * @param arenaName
     */
    public void join(Player player, String arenaName) {

        
        WinterSlashArena arena = WinterSlashGameController.getArena(arenaName);
        int maxplayers = config.getInt("arenas." + arenaName + ".maxPlayers");
        

        if (!player.hasPermission("freezetag.ftj")) {
            player.sendMessage("No permission");
            return;
        }

        if (arena == null) {
            player.sendMessage(ChatColor.RED + "This arena doesn't exist");
            return;
        }

        // make a class to save and remove inventories,
        // plug the method back here

        if (arena.getPlayers()
                .contains(player.getName())) {
            player.sendMessage(ChatColor.YELLOW
                    + "You are already in this arena!");
            return;
        }

        if (arena.isInGame()) {
            player.sendMessage(ChatColor.YELLOW
                    + "There is a game currently running in this arena!");
            return;
        }
        
        
        else {
            player.sendMessage(ChatColor.YELLOW
                    + "You have been put on the games waiting list.");

            // saves player location

            arenaData.set("PlayerData." + player.getName() + ".X", player
                    .getLocation().getBlockX());
            arenaData.set("PlayerData." + player.getName() + ".Y", player
                    .getLocation().getBlockY());
            arenaData.set("PlayerData." + player.getName() + ".Z", player
                    .getLocation().getBlockZ());
            arenaData.set("PlayerData." + player.getName() + ".Yaw", player
                    .getLocation().getYaw());
            arenaData.set("PlayerData." + player.getName() + ".Pitch", player
                    .getLocation().getPitch());
            arenaData.set("PlayerData." + player.getName() + ".World",
                    Bukkit.getName());
            arenaData.set("PlayerData." + player.getName() + ".World",
                    Bukkit.getName());
            arenaData.options().copyDefaults(true);


            WinterSlashGameController.addPlayers(player, arenaName);

            // This starts the game
            if (arena.getPlayers().size() >= maxplayers) {

                // initiates arena manager

                ItemStack revivor = new ItemStack(Material.BLAZE_ROD, 1);
                ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);

            }
        }
    }
    
    
    public void leave(Player player){
        for(String arenas: plugin.getArenaData().getConfigurationSection("arenas").getKeys(false)){
            WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
            if(arena.getGamers().contains(player)){
                WinterSlashGameController.removePlayers(player, arena.getName());
            }
        }
    }
    
    
    /**
     * This method returns a list of all arenas available.
     * @param player
     */
    public void list(Player player){
        if(arenaData.getConfigurationSection("arenas") == null){
            player.sendMessage(ChatColor.RED + "There are no arenas.");
            return;
        }
        ConfigurationSection sec = arenaData.getConfigurationSection("arenas");
        String arenas = sec.getValues(false).keySet().toString();
        player.sendMessage(Color.blue + arenas);
    }

    
    
    
    /**
     * This method sets the maximum number of 
     * players needed for a game to start
     * @param maxplayernumber
     */
    public void playerNumber(int maxplayernumber, Player player) {

        config.set("MinPlayerNumber", maxplayernumber);
        config.options().copyDefaults(true);
        player.sendMessage(ChatColor.YELLOW + "Player number set.");
    }
    
    
    
    
    
    
}
