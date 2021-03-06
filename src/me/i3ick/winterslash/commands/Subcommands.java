package me.i3ick.winterslash.commands;


import java.io.File;
import java.io.IOException;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.OfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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

        FileConfiguration config = plugin.getConfig();
        
        WinterSlashArena arena = gameController.getArena(arenaName);
        int maxplayers = config.getInt("arenas." + arenaName + ".maxPlayers");
        


        if (arena == null) {
            player.sendMessage(ChatColor.RED + "This arena doesn't exist");
            return;
        }

        if (gameController.playersInGame.contains(player.getName())){
            player.sendMessage(ChatColor.YELLOW + "You are already in a game!");
            return;
        }
        
        if (arena.getPlayers()
                .contains(player.getName())) {
            player.sendMessage(ChatColor.YELLOW + "You are already in this arena!");
            return;
        }
        if (arena.isInGame()) {
            player.sendMessage(ChatColor.YELLOW + "There is a game currently running in this arena!");
            return;
        }

        else {
            player.sendMessage(ChatColor.YELLOW + "You have been put on the games waiting list.");
            gameController.addPlayers(player, arenaName);
        }
    }


    /**
     * Method for joining a random arena.
     * @param player
     */

    public void joinRandom(Player player) {

        WinterSlashArena arena = gameController.getAvailableArena();
        if(!(arena == null)){
            this.join(player, arena.getName());
            return;
        }
        player.sendMessage("All arenas are busy at this moment!");
    }
    
    
    /**
     * Removes the player from the game and teleports him to his initial
     * location.
     * @param player
     */
    
    public void leave(Player player){
        for (String arenas: gameController.arenaNameList) {
            WinterSlashArena arena = gameController.getArena(arenas);
            if(arena.getGamers() == null){
                return;
            }
            if(arena.getGamers().contains(player.getName())){
                gameController.removePlayer(player, arena.getName());
                gameController.playersInGame.remove(player.getName());
                gameController.stats.remove(player.getName());
                return;
            }
        }
        player.sendMessage(ChatColor.YELLOW + "You are not in an arena!");
    }
    
    
    /**
     * This method force starts the specified arena
     * @param player
     * @param arenaName
     */
    public void forceStart(Player player, String arenaName){
        if(gameController.getArena(arenaName).getPlayers().isEmpty()){
            player.sendMessage(ChatColor.YELLOW + "Can't start empty arena!");
            return;
        }
        player.sendMessage(ChatColor.GREEN + "Arena " + arenaName + " has been force started!");
        gameController.startArena(arenaName);
    }
    
    /**
     * This method forces all the players to join the match
     * @param player
     * @param arenaName
     */
    public void joinAll(Player player, String arenaName){
        player.sendMessage(ChatColor.AQUA + "All players forced to join game!");
        gameController.getJoinAll(arenaName);
    }
    
    
    /**
     * This method returns a list of all arenas available.
     * @param player
     */
    public void list(Player player){
        
        if(gameController.arenaNameList.isEmpty()){
            player.sendMessage(ChatColor.RED + "There are no arenas.");
            return;
        }else{
            player.sendMessage(ChatColor.GRAY + "The following arenas are available:");
            for(String arena : gameController.arenaNameList){
            player.sendMessage(arena);
        }
        }
    }

    /**
     * Set the money award
     * @param number
     */
    
    public void setAward(double number, Player sender){
        if(!plugin.setupEconomy()){
            sender.sendMessage(ChatColor.RED + "Vault is disabled!");
            return;
        }
        plugin.getConfig().set("Settings." + "Award", number);
        gameController.awardAmount.clear();
        gameController.awardAmount.put("amount", new Double(number));
        plugin.saveConfig();
        sender.sendMessage(ChatColor.YELLOW + "Win award is now set to be " + number + "$!");
    }




    /**
     * Removes an arena
     * @param Arenaname
     * @param sender
     */

    public void removeArena(String Arenaname, Player sender){
        FileConfiguration arenaData = plugin.getArenaData();
        for(String arenas: arenaData.getConfigurationSection("arenas").getKeys(false)){ 
            if(arenas.equals(Arenaname)){
                arenaData.getConfigurationSection("arenas").set(Arenaname, null); 
                File f = new File(plugin.getDataFolder() + File.separator + "arenaData.yml");
                try {
                   arenaData.save(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gameController.arenaObjects.remove(Arenaname);
                gameController.arenaNameList.remove(Arenaname);
                sender.sendMessage(ChatColor.YELLOW + "Arena " + Arenaname + " sucessfully deleted!");
                return;
            }
        }
        sender.sendMessage(ChatColor.RED + "No such arena.");
        
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
    
    /**
     * Set red spawn.
     * @param player
     */
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
    
    /**
     * Set green spawn.
     * @param player
     */
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
    
    /**
     * Set lobby.
     * @param player
     */
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
    
    /**
     * Display mod commands.
     * @param player
     */
    public void helpMod(Player player){
        player.sendMessage(ChatColor.GOLD + "WINTERSLASH MODERATOR COMMANDS");
        player.sendMessage("/ws list");
        player.sendMessage("/ws join");
        player.sendMessage("/ws join <arenaname>");
        player.sendMessage("/ws leave");
        player.sendMessage("/ws remove <arenaname>");
        player.sendMessage("/ws setred");
        player.sendMessage("/ws setgreen");
        player.sendMessage("/ws setlobby");
        player.sendMessage("/ws award <amount>");
        player.sendMessage("/ws create <arenaname> <minimim playernumber>");
        player.sendMessage("/ws fs <arenaname>");
        player.sendMessage("/ws end <arenaname>");
        player.sendMessage("/ws joinall <arenaname>" + ChatColor.RED + " --> DEBUG ONLY!!!");
        
    }
    
    /**
     * Display user commands
     * @param player
     */
    public void helpPlayer(Player player){
        player.sendMessage(ChatColor.BLUE + "WINTERSLASH USER COMMANDS");
        player.sendMessage("/ws list");
        player.sendMessage("/ws join");
        player.sendMessage("/ws join <arenaname>");
        player.sendMessage("/ws leave");
    }




    /**
     * Force ends the arena
     * @param player
     * @param arenaName
     */
    public void forceEnd(Player player, String arenaName) {
        
        if(gameController.getArena(arenaName).isInGame() == true){
            gameController.endKick(gameController.getArena(arenaName).getName());
            gameController.endArena(gameController.getArena(arenaName).getName());
            return;
        }else{
            player.sendMessage(ChatColor.YELLOW + "Can't end an empty arena!");
        }
        
    }
    
    
    
    
    
}
