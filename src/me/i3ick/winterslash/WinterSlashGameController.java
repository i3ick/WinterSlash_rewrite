package me.i3ick.winterslash;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitScheduler;

public class WinterSlashGameController {
    
    static WinterSlashMain plugin;
    
    public WinterSlashGameController(WinterSlashMain passPlugin){
        WinterSlashGameController.plugin = passPlugin;
    }
    
  public WinterSlashArena getArena(String name){
       WinterSlashArena obj = null;
       if(this.arenaObjects.containsKey(name)){
           obj = this.arenaObjects.get(name);
           String a = obj.getGreenSpawn().toString();
           
       }
        return obj;
    }
  
  
    public ArrayList<String> arenaNameList = new ArrayList<String>();
    public ArrayList<String> playersInGame = new ArrayList<String>();
    public HashMap<String, WinterSlashArena> arenaObjects = new HashMap<String, WinterSlashArena>();
    public ArrayList<String> restorePlayers = new ArrayList<String>();
    public HashMap<String, Double> awardAmount = new HashMap<String, Double>();
    
    

    
    public void addRestore(String player){
        this.restorePlayers.add(player);
    }
    
    public void addName(String arenaName){
        this.arenaNameList.add(arenaName);
    }
    
    public String getArenaName(){
        for(String s: arenaNameList){
                return s;   
        }
        return null;
    }



    public static ArrayList<WinterSlashGameController> arenaNames = new ArrayList<>();

    /**
     * Adds the player to the playerlist and teleports him to lobby.
     * @param player
     * @param arenaName
     */


    public void addPlayers(Player player, String arenaName) {
        
        if (getArena(arenaName) != null) { 
        WinterSlashArena arena = getArena(arenaName);
        if (!(getArena(arenaName).isFull())) {
            if (!arena.isInGame()) {
            arena.setSign(player.getName());
            arena.setGamers(player.getName());
            Location initLoc = player.getLocation();
            arena.setInitData(player, initLoc);
            playersInGame.add(player.getName());
            Bukkit.getPlayer(player.getUniqueId()).teleport(arena.getLobbyLocation());
            arena.setPlayers(player);
            arena.setUnfrozen(player.getName());
            plugin.saveInventoryToFile(player.getInventory(), plugin.getDataFolder(), player.getName());
            player.getInventory().clear();
            arena.getAlive().add(player.getName());
         

            int playersLeft = arena.getMinPlayers() - arena.getPlayers().size();
            if (!(arena.getMinPlayers() == arena.getPlayers().size())) {
                
                arena.sendMessage(ChatColor.BLUE + player.getName() + " joned the arena!");
                arena.sendMessage(ChatColor.BLUE + "waiting for " + playersLeft + " more players!");
                return;
            }
            
            
            
            if (playersLeft == 0) {
                arena.sendMessage("Game starting in 15 seconds!");
                runDelayArena(arenaName);
                }
            }else{
                player.sendMessage(Color.yellow + "Match in progress!");
            }
        } else {
            player.sendMessage(Color.yellow + "This arena is full!");
        }
        }else{
            player.sendMessage(ChatColor.RED + "The arena you are looking for could not be found!");
        }
    }
    
    public void runDelayArena(final String arenaName) {
    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
        @Override
        public void run() {
            startArena(arenaName);
        }
    }, 300L);
    }
    
    
    /**
     * Removes the player from the arena, game, all arrays.
     * The player is returned his inventory and teleported to
     * his initial position.
     * @param player
     * @param arenaname
     */

    public void removePlayers(Player player, String arenaname) {
        if (getArena(arenaname) != null) { 
            WinterSlashArena arena = this.getArena(arenaname);
        if (arena.getPlayers().contains(player.getName())) {
        arena.removeGamers(player.getName());
        arena.getAlive().remove(player.getName());
        arena.clearSign(player.getName());
        player.getInventory().clear();
        arena.removePlayers();
        arena.setUnfrozen(player.getName());  
        Bukkit.getPlayer(player.getUniqueId()).teleport(arena.getInitData(player));
        arena.removePlayers();
        arena.sendMessage(ChatColor.BLUE + "Player " + player.getName() + " disconnected!");
        plugin.getInventoryFromFile(new File(plugin.getDataFolder(), player.getName() + ".invsave"), player);
        }
        
        else {   player.sendMessage(ChatColor.YELLOW + "You are not ingame!");
        }
        }
        else{
            player.sendMessage(ChatColor.RED + "The arena you are looking for could not be found!");
        }
    }
    
    /**
     * Start the arena(match).
     * @param arenaName
     */
    
    public void startArena(String arenaName){
        if(getArena(arenaName) != null){
            WinterSlashArena arena = getArena(arenaName);
            arena.sendMessage(ChatColor.GOLD + "Ready, set, GO!");
            arena.setInGame(true);
            arena.addPlayers();
            
            for (String p: arena.getPlayers()) {
                Player pl = Bukkit.getPlayer(p);
                if(arena.getRedTeam().contains(pl.getName())){
                    pl.sendMessage("You are in the Red Team");
                    pl.teleport(arena.getRedSpawn());                  
                }
                else{
                    pl.sendMessage("You are in the Green Team");
                    pl.teleport(arena.getGreenSpawn());
                }    
        }
            
        }else{
           plugin.getLogger().info("Can't start arena which doesn't exist!");
        }
    }
    
    /**
     * Ends the arena match
     * @param arenaName
     */
    
    
    public void endArena(String arenaName) {

        if (getArena(arenaName) != null) {

            WinterSlashArena arena = getArena(arenaName); 
            arena.sendMessage(ChatColor.GOLD + "Match is over!");
            arena.setInGame(false);

            if (arena.getGamers().size() == 0) {
                return;
            }
            if(arena.getUnfrozen().isEmpty()){
                return;
            }
            for (String p: arena.getAlive()) {
                if(p!= null){
                Player player = Bukkit.getPlayer(p);

                // Remove from game array
                arena.getAlive().remove(player.getName());
                arena.removeGamers(player.getName());
                arena.getSign().remove(player.getName());
                arena.clearSign(player.getName());
                arena.removePlayers();
                arena.setUnfrozen(player.getName());

                player.teleport(arena.getInitData(player));
                player.getInventory().clear();
                arena.getPlayers().remove(player.getName());
                
                plugin.getInventoryFromFile(new File(plugin.getDataFolder(), player.getName() + ".invsave"), player);

                }
            }
        } else {
            return;
        }
    }
    
    /**
     * Get all the spawn data from arenaData config file and puts them into an
     * object called "arenaobject"
     */
    public void loadArenas() {
        
        FileConfiguration arenaData = plugin.getArenaData();
        

        if (arenaData.getConfigurationSection("arenas") == null) {
            plugin.getLogger().info("There are no arenas.");
            return;
        }
        for (String arenaName : arenaData.getConfigurationSection("arenas").getKeys(
                false)) {

            String name = arenaData.getString("Worlds." + "World");
            World world = Bukkit.getServer().getWorld(name);

                //Arena names are keys
                double joinX = arenaData.getDouble("arenas." + arenaName + "." + "joinX");
                double joinY = arenaData.getDouble("arenas." + arenaName + "." + "joinY");
                double joinZ = arenaData.getDouble("arenas." + arenaName + "." + "joinZ");
                float jYaw = (float) arenaData.getDouble("arenas." + arenaName + "." + "jYaw");
                float jP = (float) arenaData.getDouble("arenas." + arenaName + "." + "jP");
                Location joinLocation = new Location(world, joinX, joinY, joinZ, jYaw, jP);
                
                double greenX = arenaData.getDouble("arenas." + arenaName + "." + "greenX");
                double greenY = arenaData.getDouble("arenas." + arenaName + "." + "greenY");
                double greenZ = arenaData.getDouble("arenas." + arenaName + "." + "greenZ"); 
                float gYaw = (float) arenaData.getDouble("arenas." + arenaName + "." + "gYaw");
                float gP = (float) arenaData.getDouble("arenas." + arenaName + "." + "gP");
                Location greenLocation = new Location(world, greenX, greenY, greenZ, gYaw, gP);
                
                double redX = arenaData.getDouble("arenas." + arenaName + "." + "redX");
                double redY = arenaData.getDouble("arenas." + arenaName + "." + "redY");
                double redZ = arenaData.getDouble("arenas." + arenaName + "." + "redZ");
                float rYaw = (float) arenaData.getDouble("arenas." + arenaName + "." + "rYaw");
                float rP = (float) arenaData.getDouble("arenas." + arenaName + "." + "rP");
                Location redLocation = new Location(world, redX, redY, redZ, rYaw, rP);
     
                 WinterSlashArena arenaobject = new WinterSlashArena();
            int minPlayers = arenaData.getInt("arenas." + arenaName + ".minPlayers");
            arenaobject.setGreen(greenLocation);
            arenaobject.setRed(redLocation);
            arenaobject.setLobby(joinLocation);
            arenaobject.setName(arenaName);
            arenaobject.minPlayers(minPlayers);
            addToHash(arenaName, arenaobject);    
            this.addName(arenaName);
            Double number = (Double) plugin.getConfig().get("Settings." + "Award");
            plugin.getLogger().info(number + "a");
            this.awardAmount.put("amount", number);
            plugin.getLogger().info(this.awardAmount.get("amount") + "adw");
        }
        plugin.getLogger().info("WinterSlash: Arenas are now loaded!");

    }
    
    
    public void addToHash(String name, WinterSlashArena arena){
        arenaObjects.put(name, arena);
    }
    
    
    /**
     * This class creates the arena
     * @param arenaName
     * @param joinLocation
     * @param redLocation
     * @param greenLocation
     * @param minPlayers
     */
    
    
    public void createArena(String arenaName, Location joinLocation,
            Location redLocation, Location greenLocation, int minPlayers) {
        
        
        FileConfiguration arenaData = plugin.getArenaData();

        arenaData.set("arenas." + arenaName, null); 
     
            String path = "arenas." + arenaName + "."; 
            arenaData.set(path + "joinX", joinLocation.getX());
            arenaData.set(path + "joinY", joinLocation.getY());
            arenaData.set(path + "joinZ", joinLocation.getZ());
            arenaData.set(path + "jYaw", joinLocation.getYaw());
            arenaData.set(path + "jP", joinLocation.getPitch());
            
            arenaData.set(path + "redX", redLocation.getX());
            arenaData.set(path + "redY", redLocation.getY());
            arenaData.set(path + "redZ", redLocation.getZ());
            arenaData.set(path + "rYaw", redLocation.getYaw());
            arenaData.set(path + "rP", redLocation.getPitch());
            
            arenaData.set(path + "greenX", greenLocation.getX());
            arenaData.set(path + "greenY", greenLocation.getY());
            arenaData.set(path + "greenZ", greenLocation.getZ());
            arenaData.set(path + "gYaw", greenLocation.getYaw());
            arenaData.set(path + "gP", greenLocation.getPitch());
            
            arenaData.set("Worlds." + "World", greenLocation.getWorld().getName());
            arenaData.set(path + "minPlayers", minPlayers);      
            
            WinterSlashArena arenaobject = new WinterSlashArena();
            arenaobject.setGreen(greenLocation);
            arenaobject.setRed(redLocation);
            arenaobject.setLobby(joinLocation);
            arenaobject.setName(arenaName);
            arenaobject.minPlayers(minPlayers);
            addToHash(arenaName, arenaobject);  
            this.addName(arenaName);
            
            File f = new File(plugin.getDataFolder() + File.separator + "arenaData.yml");
            try {
                arenaData.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
    
    
}
