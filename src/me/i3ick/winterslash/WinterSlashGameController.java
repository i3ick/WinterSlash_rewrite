package me.i3ick.winterslash;

import java.awt.Color;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.permissions.BroadcastPermissions;

public class WinterSlashGameController {
    
    WinterSlashMain plugin;
    
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


    FileConfiguration arenaData = plugin.getArenaData();

    /**
     * Adds the player to the playerlist and teleports him to lobby.
     * @param player
     * @param arenaName
     */

    @SuppressWarnings("deprecation")
    public static void addPlayers(Player player, String arenaName) {
        
        WinterSlashArena arena = getArena(arenaName);
        if (!(getArena(arenaName).isFull())) {
            arena.setSign(player.getName());
            arena.setGamers(player.getName());
            Bukkit.getPlayer(player.getName()).teleport(arena.getJoinLocation());
         

            int playersLeft = arena.getMaxPlayers() - arena.getPlayers().size();
            if (!(arena.getMaxPlayers() == arena.getPlayers().size())) {
                arena.sendMessage(ChatColor.BLUE + player.getName()
                        + " joned the arena!");
                arena.sendMessage(ChatColor.BLUE + "waiting for "
                        + " more players!");
            }
        } else {
            player.sendMessage(Color.yellow + "This arena is full!");
        }
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

    
    /**
     * Removes the player from the arena, game, all arrays.
     * The player is returned his inventory and teleported to
     * his initial position.
     * @param player
     * @param arenaname
     */

    public void removePlayers(Player player, String arenaname) {
       
        WinterSlashArena arena = WinterSlashGameController.getArena(arenaname);
        arena.removeGamers(player.getName());
        arena.clearSign(player.getName());
        player.getInventory().clear();
        
        arenaData.get("PlayerData." + player.getName() + ".X");
        
        String world = arenaData.getString("Worlds" + ".World");
        int playerX = arenaData.getInt("PlayerData." + player.getName() + ".X");
        int playerY =arenaData.getInt("PlayerData." + player.getName() + ".Y");
        int playerZ = arenaData.getInt("PlayerData." + player.getName() + ".Z");
        int playerYaw = arenaData.getInt("PlayerData." + player.getName() + ".Yaw");
        int playerPitch = arenaData.getInt("PlayerData." + player.getName() + ".Pitch");
        
        Location initialPos = new Location(Bukkit.getWorld(world), playerX, playerY, playerZ, playerPitch, playerYaw);     
        Bukkit.getPlayer(player.getName()).teleport(initialPos);
        
    }
    
    
    
}
