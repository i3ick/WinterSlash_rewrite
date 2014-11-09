package me.i3ick.winterslash;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import me.i3ick.winterslash.commands.MainCommand;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class WinterSlashMain extends JavaPlugin {


    @Override
    public void onDisable() {

        File f = new File(this.getDataFolder() + File.separator + "arenaData.yml");
        FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(f);
        getLogger().info("WinterSlash Plugin Disabled!");
        try {
            arenaConfig.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        

    }

    @Override
    public void onEnable() {
        final FileConfiguration config = this.getConfig();

        try{
            File f = new File(this.getDataFolder() + File.separator + "arenaData.yml");
            if(f.exists()){
                FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(f);
            }else{
                FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(f);
                arenaConfig.addDefault("Redspawn" + ".X", null);
                arenaConfig.addDefault("Worlds" + ".World", null);
                arenaConfig.addDefault("Greenspawn" + ".X", null);
                arenaConfig.addDefault("Lobby" + ".X", null);
                arenaConfig.addDefault("arenas" + ".X", null);
                arenaConfig.addDefault("PlayerData" + ".X", null);
                arenaConfig.addDefault("DeathPosition" + ".X", null);
                arenaConfig.addDefault("MinPlayerNumber" + ".X", null);
                arenaConfig.options().copyDefaults(true);
                arenaConfig.save(f);
                
            }
        } catch(Exception e){
            getLogger().info("Error loading arenaData.yml. File not found!");
        }
        
        

        // load world
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            getLogger().info("config exists and loaded!");
            this.getConfig().options().copyDefaults(true);
            this.saveConfig();
            saveDefaultConfig();
        }
        
        
        String playerWorld = config.getString("Worlds" + ".World");
        WorldCreator c = new WorldCreator(playerWorld);
        c.createWorld();

        getLogger().info("WinterSlash: Worldname: " + playerWorld);

        getLogger().info("registering");
        // register commands
        this.getCommand("ws").setExecutor(new MainCommand(this));
        getLogger().info("rdwad");

        // register events
        this.getServer().getPluginManager().registerEvents(new WinterSlashEvents(this), this);
        this.getServer().getPluginManager().registerEvents(new WinterSlashSigns(this), this);
        
        // load arena's   
        FileConfiguration arenaData = this.getArenaData();

        if (arenaData.getConfigurationSection("arenas") == null) {
            this.getLogger().info("There are no arenas.");
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
                 
     
            int minPlayers = arenaData.getInt("arenas." + arenaName + ".minPlayers");

            WinterSlashArena arenaobject = new WinterSlashArena(arenaName,
                    joinLocation, redLocation, greenLocation, minPlayers);
           
            int maxplayernumber = config.getInt("MaxPlayerNumber");
          //  arena.setMaxPlayers(maxplayernumber);
            
        }
        this.getLogger().info("WinterSlash: Arenas are now loaded!");

        
        
        getLogger().info("Plugin Enabled!");
    }


    public FileConfiguration getArenaData(){
        File f = new File(this.getDataFolder() + File.separator + "arenaData.yml");
        FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(f);
        return arenaConfig;
    }
    

    public static boolean isInventoryEmpty(Player p) {
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null)
                return true;
        }
        return false;
    }
    
    public void saveArenaData(){
    File f = new File(this.getDataFolder() + File.separator + "arenaData.yml");
    FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(f);
    try {
        arenaConfig.save(f);
        getLogger().info("file saved!");
    } catch (IOException e) {
        e.printStackTrace();
    }
    }


}
