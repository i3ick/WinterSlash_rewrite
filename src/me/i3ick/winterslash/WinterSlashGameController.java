package me.i3ick.winterslash;

import java.awt.Color;
import java.util.List;

import me.i3ick.com.WinterSlashArena;
import me.i3ick.com.WinterSlashMain;
import me.i3ick.com.WinterSlashManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class WinterSlashGameController {
    
    WinterSlashMain plugin;
    
    private static WinterSlashGameController _app;
    
    public WinterSlashGameController( WinterSlashMain passPlugin){
        this.plugin = passPlugin;
    }
    
    
    public static WinterSlashArena getArena(String name){
        for(WinterSlashArena a: WinterSlashArena.arenaObjects){
            if(a.getName().equals(name)){
                return a;
            }
        }
        return null;
    }


    public FileConfiguration arenaData = plugin.getArenaData();

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
            Bukkit.getPlayer(player.getUniqueId()).teleport(arena.getJoinLocation());
            arena.setPlayers(player);
            arena.setUnfrozen(player.getName());
         

            int playersLeft = arena.getMaxPlayers() - arena.getPlayers().size();
            if (!(arena.getMaxPlayers() == arena.getPlayers().size())) {
                
                arena.sendMessage(ChatColor.BLUE + player.getName() + " joned the arena!");
                arena.sendMessage(ChatColor.BLUE + "waiting for " + playersLeft + " more players!");
                return;
            }
            
            if (playersLeft == 0) {
                startArena(arenaName); 
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
    
    
    
    /**
     * Removes the player from the arena, game, all arrays.
     * The player is returned his inventory and teleported to
     * his initial position.
     * @param player
     * @param arenaname
     */

    public void removePlayers(Player player, String arenaname) {
        
        if (getArena(arenaname) != null) { 
       
        WinterSlashArena arena = WinterSlashGameController.getArena(arenaname);
        if (arena.getPlayers().contains(player.getName())) {
        arena.removeGamers(player.getName());
        arena.clearSign(player.getName());
        player.getInventory().clear();
        
        arenaData.get("PlayerData." + player.getUniqueId() + ".X");
        
        String world = arenaData.getString("Worlds" + ".World");
        int playerX = arenaData.getInt("PlayerData." + player.getUniqueId() + ".X");
        int playerY =arenaData.getInt("PlayerData." + player.getUniqueId() + ".Y");
        int playerZ = arenaData.getInt("PlayerData." + player.getUniqueId() + ".Z");
        int playerYaw = arenaData.getInt("PlayerData." + player.getUniqueId() + ".Yaw");
        int playerPitch = arenaData.getInt("PlayerData." + player.getUniqueId() + ".Pitch");
        
        Location initialPos = new Location(Bukkit.getWorld(world), playerX, playerY, playerZ, playerPitch, playerYaw);     
        Bukkit.getPlayer(player.getUniqueId()).teleport(initialPos);
        arena.removePlayers();
        arena.sendMessage(ChatColor.BLUE + "Player " + player.getName() + " disconnected!");
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
                if(arena.GetRedTeam().contains(pl.getName())){
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
     
    WinterSlashArena arena = getArena(arenaName); //Create an arena for using in this method
    arena.sendMessage(ChatColor.GOLD + "Match is over!");
    arena.setInGame(false);
    
    if(arena.getGamers().size() == 0){
        return;
    }

    for (String p: arena.GetAlive()) {
    Player player = Bukkit.getPlayer(p); 
    String world = player.getLocation().getWorld().getName();
    int playerX = plugin.getArenaData().getInt("DeathPosition." + player.getUniqueId() + ".X");
    int playerY = plugin.getArenaData().getInt("DeathPosition." + player.getUniqueId() + ".Y");
    int playerZ = plugin.getArenaData().getInt("DeathPosition." + player.getUniqueId() + ".Z");
    int playerYaw = plugin.getArenaData().getInt("DeathPosition." + player.getUniqueId() + ".Yaw");
    int playerPitch = plugin.getArenaData().getInt("DeathPosition." + player.getUniqueId() + ".Pitch");
    Location out = new Location(Bukkit.getWorld(world), playerX, playerY, playerZ, playerYaw, playerPitch);

    //return inventory
    @SuppressWarnings("unchecked")
    List<ItemStack> inv = (List<ItemStack>) config.get("PlayerData." + player.getName() + ".inventory");
    ItemStack[] stacks = inv.toArray(new ItemStack[inv.size()]);
    player.getInventory().setContents(stacks);
    
    //Remove from game array
    arena.removeGamers(player.getName());
    arena.getSign().remove(player.getName());
    arena.removePlayers();
    arena.setFrozen(player.getName());
    

    player.teleport(out);
    player.getInventory().clear(); 
    //Remove them all from the list
    arena.getPlayers().remove(player.getName());
     
    }
    }else{return;}
    }
    
    
    /**
     * Get all the spawn data from arenaData config file and
     * puts them into an object called "arenaobject"
     */
    public void loadArenas(){
        
        if(arenaData.getConfigurationSection("arenas") == null){
            plugin.getLogger().info("There are no arenas.");
            return;
        }
        
    for (String keys: arenaData.getConfigurationSection("arenas").getKeys(false)) {
     

    String name = arenaData.getString("Worlds." + "World");
    World world = Bukkit.getServer().getWorld(name);
     
    //Arena names are keys
    double joinX = arenaData.getDouble("arenas." + keys + "." + "joinX");
    double joinY = arenaData.getDouble("arenas." + keys + "." + "joinY");
    double joinZ = arenaData.getDouble("arenas." + keys + "." + "joinZ");
    float jYaw = (float) arenaData.getDouble("arenas." + keys + "." + "jYaw");
    float jP = (float) arenaData.getDouble("arenas." + keys + "." + "jP");
    Location joinLocation = new Location(world, joinX, joinY, joinZ, jYaw, jP);
    
    double greenX = arenaData.getDouble("arenas." + keys + "." + "greenX");
    double greenY = arenaData.getDouble("arenas." + keys + "." + "greenY");
    double greenZ = arenaData.getDouble("arenas." + keys + "." + "greenZ"); 
    float gYaw = (float) arenaData.getDouble("arenas." + keys + "." + "gYaw");
    float gP = (float) arenaData.getDouble("arenas." + keys + "." + "gP");
    Location greenLocation = new Location(world, greenX, greenY, greenZ, gYaw, gP);
    
    double redX = arenaData.getDouble("arenas." + keys + "." + "redX");
    double redY = arenaData.getDouble("arenas." + keys + "." + "redY");
    double redZ = arenaData.getDouble("arenas." + keys + "." + "redZ");
    float rYaw = (float) arenaData.getDouble("arenas." + keys + "." + "rYaw");
    float rP = (float) arenaData.getDouble("arenas." + keys + "." + "rP");
    Location redLocation = new Location(world, redX, redY, redZ, rYaw, rP);
     
     

    int maxPlayers = arenaData.getInt("arenas." + keys + ".maxPlayers");
     

    WinterSlashArena arenaobject = new WinterSlashArena(keys, joinLocation, redLocation, greenLocation, maxPlayers);
    }
    plugin.getLogger().info("WinterSlash: Arenas are now loaded!");
        
    }

    
    
    
    
}
