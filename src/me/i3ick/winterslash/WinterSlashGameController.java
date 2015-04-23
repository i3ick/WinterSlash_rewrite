package me.i3ick.winterslash;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.NBTInputStream;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import sun.security.krb5.internal.ccache.Tag;

import javax.swing.*;

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

    public WinterSlashArena getAvailableArena(){
        WinterSlashArena obj = null;

        for(int i = 0; i < arenaNameList.size(); i=i+1){
            String name = arenaNameList.get(i);
            WinterSlashArena arena = getArena(name);
            if(!(arena.isInGame())){
                return arena;
            }
        }
        return obj;
    }
  
  public WinterSlashPlayerInfo getPlayerData(String name){
      WinterSlashPlayerInfo obj = null;
      if(this.stats.containsKey(name)){
          obj = this.stats.get(name);
      }
       return obj;
   }
  
  
  public KillStreakWolfLogic getWolfData(String name){
      KillStreakWolfLogic obj = null;
      if(this.wolfObject.containsKey(name)){
          obj = this.wolfObject.get(name);
      }
       return obj;
   }
  
  
    public ArrayList<String> arenaNameList = new ArrayList<String>();
    public ArrayList<String> playersInGame = new ArrayList<String>();
    public HashMap<String, Location> userBlockLoc = new HashMap<String, Location>();
    public HashMap<Location, Material> blockRestore = new HashMap<Location, Material>();
    public HashMap<Location, Byte> blockRestoreByte = new HashMap<Location, Byte>();
    public HashMap<String, KillStreakWolfLogic> wolfObject = new HashMap<String, KillStreakWolfLogic>();
    public HashMap<String, WinterSlashArena> arenaObjects = new HashMap<String, WinterSlashArena>();
    public HashMap<String, WinterSlashPlayerInfo> stats = new HashMap<String, WinterSlashPlayerInfo>();
    public ArrayList<String> restorePlayers = new ArrayList<String>();
    public HashMap<String, Double> awardAmount = new HashMap<String, Double>();
    public ArrayList<String> Winner = new ArrayList<String>();
    public ArrayList<String> Heavy = new ArrayList<String>();
    public ArrayList<String> Light = new ArrayList<String>();
    public ArrayList<String> Archer = new ArrayList<String>();
    Map<Player, Location> PlayerInitData = new HashMap<Player, Location>();
    public HashMap<String, ItemStack[]> PlayerArmor = new HashMap<String, ItemStack[]>();
    public int lastRound;
    
    public void setInitData(Player player, Location loc){
        PlayerInitData.put(player, loc);
    }
    
    public Location getInitData(Player player){
           return PlayerInitData.get(player);  
    }
    

    
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
            if (!arena.isInGame()) {
            arena.setSign(player.getName());
            arena.setGamers(player.getName());
            Location initLoc = player.getLocation();
            setInitData(player, initLoc);
            playersInGame.add(player.getName());
            arena.disableFire.add(player.getName());
            Bukkit.getPlayer(player.getUniqueId()).teleport(arena.getLobbyLocation());
            arena.setPlayers(player);
            arena.setUnfrozen(player.getName());
            plugin.saveInventoryToFile(player.getInventory(), plugin.getDataFolder(), player.getName(), player.getGameMode());
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            arena.getAlive().add(player.getName());
            PlayerArmor.put(player.getName(), player.getInventory().getArmorContents());
            ItemStack nothing = new ItemStack(Material.AIR, 1);
            player.getInventory().setHelmet(nothing);
            player.getInventory().setChestplate(nothing);
            player.getInventory().setLeggings(nothing);
            player.getInventory().setBoots(nothing);
                player.setHealth(20.0);
                player.setFoodLevel(20);
         

            int playersLeft = arena.getMinPlayers() - arena.getPlayers().size();
            if (!(arena.getMinPlayers() <= arena.getPlayers().size())) {

                player.sendMessage("Connected to " + arena.getName() + " arena.");
                arena.sendMessage(ChatColor.BLUE + player.getName() + " joined the arena!");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName() + " times 5 18 5");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName() + " title [{text:Winter,color:aqua,bold:true},{text:Slash,color:white,bold:false}]");
                if(playersLeft > 0){
                    arena.sendMessage(ChatColor.BLUE + "waiting for " + playersLeft + " more players!");
                }
                return;
            }
            
            
            if (playersLeft == 0) {
                arena.sendMessage("Game starting in 15 seconds!");
                runDelayArena(arenaName);
                }
            }else{
                player.sendMessage(ChatColor.YELLOW + "Match in progress!");
                plugin.getLogger().info(arena.getPlayers().size() + "d");
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
            WinterSlashArena arena = getArena(arenaName);
            if (!(arena.isInGame())) {
                startArena(arenaName);

            }
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

    public void removePlayer(Player player, String arenaname) {
        plugin.getLogger().info("removePlayers");
        if (getArena(arenaname) != null) { 
            WinterSlashArena arena = this.getArena(arenaname);
        if (arena.getPlayers().contains(player.getName())) {
        arena.removeGamers(player.getName());
        arena.getAlive().remove(player.getName());
        arena.clearSign(player.getName());
            player.getInventory().clear();
        arena.getUnfrozen().add(player.getName());
        playersInGame.remove(player.getName());
            player.setHealth(20.0);
            player.setFoodLevel(20);
        
        if(Light.contains(player)){
            Light.remove(player);
        }
        
        if(Heavy.contains(player)){
            Heavy.remove(player);
        }

        if(Archer.contains(player)){
            Archer.remove(player);
        }
        
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
        
        Bukkit.getPlayer(player.getUniqueId()).teleport(getInitData(player));
        arena.sendMessage(ChatColor.BLUE + "Player " + player.getName() + " disconnected!");
        plugin.getInventoryFromFile(new File(plugin.getDataFolder(), player.getName() + ".invsave"), player);
        ItemStack nothing = new ItemStack(Material.AIR, 1);
        player.getInventory().setHelmet(nothing);
        player.getInventory().setChestplate(nothing);
        player.getInventory().setLeggings(nothing);
        player.getInventory().setBoots(nothing);
        player.getInventory().setArmorContents(PlayerArmor.get(player));
        player.sendMessage(ChatColor.BLUE + "Disconnected.");
        
        if(arena.getRedTeam().contains(player.getName())){
            arena.getRedTeam().remove(player.getName());
        }
        
        if(arena.getGreenTeam().contains(player.getName())){
            arena.getGreenTeam().remove(player.getName());
        }
        
        if(arena.getRedTeam().isEmpty() || arena.getGreenTeam().isEmpty()){
            endKick(arena.getName());
            endArena(arena.getName());
        } 
        return;
        }
            
        }
        else{
            player.sendMessage(ChatColor.RED + "The arena you are looking for could not be found!");
        }
    }
    
    
    /**
     * Removes the player who forcibly lost connection or
     * disconnected the game from the arena, game and all arrays.
     * The player is returned his inventory.
     * @param player
     * @param arenaname
     */

    public void forceRemovePlayers(Player player, String arenaname) {
        plugin.getLogger().info("forceRemovePlayers");
        if (getArena(arenaname) != null) { 
            WinterSlashArena arena = this.getArena(arenaname);
        if (arena.getPlayers().contains(player.getName())) {
        arena.removeGamers(player.getName());
        arena.getAlive().remove(player.getName());
        arena.clearSign(player.getName());
        player.getInventory().clear();
        arena.getUnfrozen().add(player.getName());
        playersInGame.remove(player.getName());
        
        if(Light.contains(player)){
            Light.remove(player);
        }
        
        if(Heavy.contains(player)){
            Heavy.remove(player);
        }

        if(Archer.contains(player)){
            Archer.remove(player);
        }
        
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
        
        Bukkit.getPlayer(player.getUniqueId()).teleport(getInitData(player));
        arena.sendMessage(ChatColor.BLUE + "Player " + player.getName() + " disconnected!");
        plugin.getInventoryFromFile(new File(plugin.getDataFolder(), player.getName() + ".invsave"), player);
        ItemStack nothing = new ItemStack(Material.AIR, 1);
        player.getInventory().setHelmet(nothing);
        player.getInventory().setChestplate(nothing);
        player.getInventory().setLeggings(nothing);
        player.getInventory().setBoots(nothing);
        player.getInventory().setArmorContents(PlayerArmor.get(player));
        
        if(arena.getRedTeam().contains(player.getName())){
            arena.getRedTeam().remove(player.getName());
        }
        
        if(arena.getGreenTeam().contains(player.getName())){
            arena.getGreenTeam().remove(player.getName());
        }
                return;
            }
        }
    }
    
    /**
     * Removes the DEAD player from the arena, game, all arrays.
     * The player is returned his inventory and teleported to
     * his initial position.
     * @param player
     * @param arenaname
     */

    public void removeDeadPlayers(Player player, String arenaname) {
        plugin.getLogger().info("removeDeadPlayers");
        if (getArena(arenaname) != null) { 
            WinterSlashArena arena = this.getArena(arenaname);
        if (arena.getPlayers().contains(player.getName())) {
        arena.removeGamers(player.getName());
        arena.clearSign(player.getName());
        player.getInventory().clear();
        arena.getUnfrozen().remove(player.getName());
        arena.getPlayers().remove(player.getName());
        playersInGame.remove(player.getName());
        this.stats.remove(player.getName());
        if(Light.contains(player)){
            Light.remove(player);
        }
        
        if(Heavy.contains(player)){
            Heavy.remove(player);
        }

        if(Archer.contains(player)){
            Archer.remove(player);
        }
        
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
        
        plugin.getInventoryFromFile(new File(plugin.getDataFolder(), player.getName() + ".invsave"), player);
        ItemStack nothing = new ItemStack(Material.AIR, 1);
        player.getInventory().setHelmet(nothing);
        player.getInventory().setChestplate(nothing);
        player.getInventory().setLeggings(nothing);
        player.getInventory().setBoots(nothing);
        player.getInventory().setArmorContents(PlayerArmor.get(player));
        player.sendMessage(ChatColor.BLUE + "Disconnected.");
        
        if(arena.getGamers().size() == 0){
            endArena(arenaname);
        }
        
        return;
        }
        }
        else{
            player.sendMessage(ChatColor.RED + "The arena you are looking for could not be found!");
        }
        return;
    }
    
    
    /**
     * Start the arena(match).
     * @param arenaName
     */
    
    public void startArena(String arenaName){
        if(getArena(arenaName) != null){
            WinterSlashArena arena = getArena(arenaName);
            arena.disableFire.clear();
            WinterSlashClasses classes = new WinterSlashClasses();
            arena.sendMessage(ChatColor.GOLD + "Ready, set, GO!");
            arena.setInGame(true);
            arena.addPlayers();
            plugin.getLogger().info(arena.getGamers().toString());

            if(arena.getPlayers().size() < 6){
                this.lastRound = 4;
            }
            if(arena.getPlayers().size() > 6 & arena.getPlayers().size() < 8){
                this.lastRound = 2;
            }

            for (String p: arena.getGamers()) {
                Player pl = Bukkit.getPlayer(p);  
                WinterSlashPlayerInfo stats = new WinterSlashPlayerInfo(this);
                stats.setName(pl.getName());
                addToPlayerHash(pl.getName(), stats);

                if(arena.getRedTeam().contains(pl.getName())) {
                    pl.sendMessage("You are in the Red Team");
                    pl.teleport(arena.getRedSpawn());
                    classes.redArmor(pl);
                    if(!(Light.contains(pl)) || (!Heavy.contains(pl)) || (!Archer.contains(pl))){
                        classes.setDefault(pl);
                    }
                } else {
                    pl.sendMessage("You are in the Green Team");
                    pl.teleport(arena.getGreenSpawn());
                    classes.greenArmor(pl);
                    if((!Light.contains(pl)) || (!Heavy.contains(pl)) || (!Archer.contains(pl)) ){
                        classes.setDefault(pl);
                    }
                }
                arena.sendBroadcastMessage(1);
        }
            
        }else{
           plugin.getLogger().info("Can't start arena which doesn't exist!");
        }
    }
    
    /**
     * Adds all the players to the game
     */
    public void getJoinAll(String arenaName){
        for(Player n :Bukkit.getServer().getOnlinePlayers()){
            WinterSlashArena arena = getArena(arenaName); 
            this.addPlayers(n, arenaName);
        }
        return;
    }
    
    
    /**
     * Ends the arena match
     * @param arenaName
     */
    public void endArena(String arenaName) {
        plugin.getLogger().info("endArena");
        if (getArena(arenaName) != null) {

            WinterSlashArena arena = getArena(arenaName); 
            
         /*   if(arena.getUnfrozen().isEmpty()){
                return;
            } */
            removeChests();
            arena.reset();
            arena.setInGame(false);
            plugin.getLogger().info("cleared!");
        } else {
            return;
        }
    }

    public void removeChests(){

        for(Map.Entry<Location, Material> chestPosInfo: blockRestore.entrySet()) {
            chestPosInfo.getKey().getBlock().setType(Material.AIR);
            chestPosInfo.getKey().getBlock().getRelative(BlockFace.DOWN).setType(chestPosInfo.getValue());

            Block upd = chestPosInfo.getKey().getBlock().getRelative(BlockFace.DOWN);
            int byt = blockRestoreByte.get(chestPosInfo.getKey().getBlock().getLocation());
            upd.setData((byte) byt);
        }
    }
    
    /**
     * Kicks out all the players before ending the arena
     * @param arenaName
     */
    
    public void endKick(String arenaName) {
        plugin.getLogger().info("endKick");
        if (getArena(arenaName) != null) {

            WinterSlashArena arena = getArena(arenaName); 
            arena.sendMessage(ChatColor.GOLD + "Match is over!");

            if (arena.getGamers().size() == 0) {
                return;
            }
            if(arena.getUnfrozen().isEmpty()){
                return;
            }
            
            List<String> playerList = arena.getAlive();
            
            for (String p: playerList) {
                if(p!= null){
                Player player = Bukkit.getPlayer(p);

                arena.removeGamers(player.getName());
                arena.getSign().remove(player.getName());
                arena.clearSign(player.getName());
                arena.getUnfrozen().remove(player.getName());
                playersInGame.remove(player.getName());
                this.stats.remove(player.getName());
                
                if(Light.contains(player)){
                    Light.remove(player);
                }
                
                if(Heavy.contains(player)){
                    Heavy.remove(player);
                }

                if(Archer.contains(player)){
                    Archer.remove(player);
                }
                
                for (PotionEffect effect : player.getActivePotionEffects())
                    player.removePotionEffect(effect.getType());
                
                player.teleport(getInitData(player));
                player.getInventory().clear();
                ItemStack nothing = new ItemStack(Material.AIR, 1);
                player.getInventory().setHelmet(nothing);
                player.getInventory().setChestplate(nothing);
                player.getInventory().setLeggings(nothing);
                player.getInventory().setBoots(nothing);
                player.getInventory().setArmorContents(PlayerArmor.get(p));
                arena.getPlayers().remove(player.getName());               
                plugin.getInventoryFromFile(new File(plugin.getDataFolder(), player.getName() + ".invsave"), player);
                player.sendMessage(ChatColor.BLUE + "Disconnected.");
                
                if(arena.getRedTeam().contains(player.getName())){
                    arena.getRedTeam().remove(player.getName());
                }
                
                if(arena.getGreenTeam().contains(player.getName())){
                    arena.getGreenTeam().remove(player.getName());
                }
                
                }
            }
            playerList.clear();
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
            this.awardAmount.put("amount", number);
        }
        plugin.getLogger().info("WinterSlash: Arenas are now loaded!");

    }
    
    
    public void addToHash(String name, WinterSlashArena arena){
        arenaObjects.put(name, arena);
    }
    
    public void addToPlayerHash(String name, WinterSlashPlayerInfo data){
        stats.put(name, data);
    }
    
    
    public void addToWolfHash(String name, KillStreakWolfLogic wolf){
        wolfObject.put(name, wolf);
        plugin.getLogger().info("atthashto" + wolfObject.get(name).getName());
    }
    
    
    
    /**
     * This method awards the winners with money
     * @param player
     */
    
    public void awardMoney(Player player){
        
        if(plugin.econ == null){
            plugin.getLogger().info("can't award money, no Vault or Economy plugin detected");
            return;
        }
        
        EconomyResponse r = plugin.econ.depositPlayer(player, this.awardAmount.get("amount"));
        if(r.transactionSuccess()) {
            player.sendMessage(String.format(ChatColor.GREEN + "You were awarded %s for winning the round and now you have a total of %s", plugin.econ.format(r.amount), plugin.econ.format(r.balance)));
        } else {
            player.sendMessage(String.format("An error occured: %s", r.errorMessage));
        }
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
