package me.i3ick.winterslash.commands;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.i3ick.winterslash.WinterSlashArena;
import me.i3ick.winterslash.WinterSlashArenaCreator;
import me.i3ick.winterslash.WinterSlashGameController;
import me.i3ick.winterslash.WinterSlashMain;

public class Subcommands {

    WinterSlashMain plugin;
    WinterSlashGameController gameController;
    WinterSlashArenaCreator creator;

    public Subcommands(WinterSlashMain passPlugin, WinterSlashGameController passPlug2, WinterSlashArenaCreator passplug3) {
        this.plugin = passPlugin;
        this.gameController = passPlug2;
        this.creator = passplug3;
        
    }



 
    
    /**
     * This method runs checks on the arena status and
     * adds the player to the game lobby.
     * @param player
     * @param arenaName
     */
    public void join(Player player, String arenaName) {
        plugin.getLogger().info("1");

        FileConfiguration config = plugin.getConfig();
        FileConfiguration arenaData = plugin.getArenaData();
        
        WinterSlashArena arena = WinterSlashGameController.getArena(arenaName);
        int maxplayers = config.getInt("arenas." + arenaName + ".maxPlayers");
        

        if (!player.hasPermission("freezetag.ftj")) {
            player.sendMessage("No permission");
            return;
        }

        plugin.getLogger().info("2");
        if (arena == null) {
            player.sendMessage(ChatColor.RED + "This arena doesn't exist");
            return;
        }

        // make a class to save and remove inventories

        if (arena.getPlayers()
                .contains(player.getName())) {
            player.sendMessage(ChatColor.YELLOW + "You are already in this arena!");
            return;
        }
        plugin.getLogger().info("3");
        if (arena.isInGame()) {
            player.sendMessage(ChatColor.YELLOW + "There is a game currently running in this arena!");
            return;
        }

        else {
            player.sendMessage(ChatColor.YELLOW + "You have been put on the games waiting list.");

            arenaData.set("PlayerData." + player.getName() + ".X", player.getLocation().getBlockX());
            arenaData.set("PlayerData." + player.getName() + ".Y", player.getLocation().getBlockY());
            arenaData.set("PlayerData." + player.getName() + ".Z", player.getLocation().getBlockZ());
            arenaData.set("PlayerData." + player.getName() + ".Yaw", player.getLocation().getYaw());
            arenaData.set("PlayerData." + player.getName() + ".Pitch", player.getLocation().getPitch());
            arenaData.set("PlayerData." + player.getName() + ".World", Bukkit.getName());
            arenaData.set("PlayerData." + player.getName() + ".World", Bukkit.getName());
            arenaData.options().copyDefaults(true);
            
            
            gameController.addPlayers(player, arenaName);

            // This starts the game
            if (arena.getPlayers().size() >= maxplayers) {
            }
        }
    }
    
    /**
     * Removes the player from the game and teleports him to his initial
     * location.
     * @param player
     */
    
    public void leave(Player player){
        for(String arenas: plugin.getArenaData().getConfigurationSection("arenas").getKeys(false)){
            WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
            if(arena.getGamers().contains(player)){
                gameController.removePlayers(player, arena.getName());
            }
        }
    }
    
    
    /**
     * This method returns a list of all arenas available.
     * @param player
     */
    public void list(Player player){
        FileConfiguration arenaData = plugin.getArenaData();
        if(arenaData.getConfigurationSection("arenas") == null){
            player.sendMessage(ChatColor.RED + "There are no arenas.");
            return;
        }
        ConfigurationSection sec = arenaData.getConfigurationSection("arenas");
        String arenas = sec.getValues(false).keySet().toString();
        player.sendMessage(Color.blue + arenas);
    }

    
    

    
    
    /**
     * Removes an arena
     * @param player
     */
    
    public void removeArena(String Arenaname, Player sender){
        for(String arenas: plugin.getArenaData().getConfigurationSection("arenas").getKeys(false)){
            if(arenas.equals(Arenaname)){
                plugin.getArenaData().getConfigurationSection("arenas").set(Arenaname, null);
                sender.sendMessage(Color.yellow + "Arena " + Arenaname + " sucessfully deleted!");
            }
            else{
                sender.sendMessage(Color.red + "No such arena.");
            }
        }
        
    }
    
    public void create(String arenaName,Player player, int minimumPlayerNumber){
        

                 Location redSpawn = creator.getRedSpawn();
                 Location greenSpawn = creator.getGreenSpawn();
                 Location lobbySpawn = creator.getLobbySpawn();
                 
                 String world = player.getLocation().getWorld().getName();
                 plugin.getArenaData().set("Worlds" + ".World", world);
                 plugin.saveArenaData();
                 
                 if(world != null)
                 {
                     gameController.createArena(arenaName, lobbySpawn, redSpawn, greenSpawn, minimumPlayerNumber);
                     player.sendMessage(ChatColor.GREEN + (arenaName + " successfully created!"));
                 }
                 else
                 {                     
                    // Bukkit.getServer().createWorld(new WorldCreator(playerWorld).environment(World.Environment.NORMAL));                     
                     plugin.getLogger().warning("The '" + world + "' world from arenaData.yml does not exist or is not loaded !");
                 }

        
    }
    
    
    public void setRed(Player player){
        World world = player.getLocation().getWorld();
        double x = player.getLocation().getBlockX();
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getBlockZ();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();       
        
        Location redLocation = new Location(world, x, y, z, yaw, pitch);
        creator.redSpawn(redLocation);
        player.sendMessage(ChatColor.YELLOW + "Red spawn selected.");
    }
    
    public void setGreen(Player player){
        World world = player.getLocation().getWorld();
        double x = player.getLocation().getBlockX();
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getBlockZ();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();       
        
        Location greenLocation = new Location(world, x, y, z, yaw, pitch);
        creator.greenSpawn(greenLocation);
        player.sendMessage(ChatColor.YELLOW + "Green spawn selected.");
    }
    
    public void setLobby(Player player){
        World world = player.getLocation().getWorld();
        double x = player.getLocation().getBlockX();
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getBlockZ();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();       
        
        Location lobbyLocation = new Location(world, x, y, z, yaw, pitch);
        creator.lobbySpawn(lobbyLocation);
        player.sendMessage(ChatColor.YELLOW + "Lobby spawn selected.");
    }
    
    public void helpMod(Player player){
        player.sendMessage(ChatColor.BLUE + "WINTERSLASH MODERATOR COMMANDS");
        player.sendMessage("/ws list");
        player.sendMessage("/ws join <arenaname>");
        player.sendMessage("/ws leave");
        player.sendMessage("/ws remove <arenaname>");
        player.sendMessage("/ws setred");
        player.sendMessage("/ws setgreen");
        player.sendMessage("/ws setlobby");
        player.sendMessage("/ws create <arenaname> <minimim playernumber>");
        player.sendMessage("/ws pn <global maximum playernumber>");
        
    }
    
    public void helpPlayer(Player player){
        player.sendMessage(ChatColor.BLUE + "WINTERSLASH USER COMMANDS");
        player.sendMessage("/ws list");
        player.sendMessage("/ws join <arenaname>");
        player.sendMessage("/ws leave");
    }
    
    
    
    
    
}
