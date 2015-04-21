package me.i3ick.winterslash;

import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.i3ick.winterslash.commands.MainCommand;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.mysql.jdbc.log.LogUtils;

public class WinterSlashMain extends JavaPlugin {
    
    public static Economy econ = null;
    public static WorldEditPlugin worldEdit = null;
    private static final Logger log = Logger.getLogger("Minecraft");

    

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
        
        if (!setupEconomy() ) {
            log.severe(String.format("Money disabled due to no Vault dependency found!", getDescription().getName()));
          //  getServer().getPluginManager().disablePlugin(this);
           // return;
        }

        if (!setupWorldEdit() ) {
            log.severe(String.format("WorldEdit not found. You won't be able to generate arena throught schematics.", getDescription().getName()));
        }
        
        

        // load world
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            getLogger().info("config exists and loaded!");
            this.getConfig().options().copyDefaults(true);
            this.saveConfig();
            saveDefaultConfig();
        }
        
        
      /*  String playerWorld = config.getString("Worlds" + ".World");
        WorldCreator c = new WorldCreator(playerWorld);
        c.createWorld();

        getLogger().info("WinterSlash: Worldname: " + playerWorld);

        */
        
        // load arena's      
        WinterSlashGameController gameController = new WinterSlashGameController(this);
        WinterSlashClasses classes = new WinterSlashClasses();
        gameController.loadArenas();
        
        WinterSlashArenaCreator creator = new WinterSlashArenaCreator(this);
        
        // register commands
        this.getCommand("ws").setExecutor(new MainCommand(this, creator, gameController));
        

        // register events
        this.getServer().getPluginManager().registerEvents(new WinterSlashEvents(this, gameController), this);
        this.getServer().getPluginManager().registerEvents(new WinterSlashSigns(this, gameController, classes), this);

        PluginDescriptionFile pdf = this.getDescription();
        getLogger().info("\n\n#####~~~~~~ WINTER-SLASH ~~~~~~##### \n" +
                         "   WinterSlash version " + pdf.getVersion() + " is now running!\n" +
                         "   Vault enabled: " + setupEconomy() +
                         "\n\n" + "#####~~~~~~~~ by i3ick ~~~~~~~~#####"

        );
    }

    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


    private boolean setupWorldEdit() {
        if (getServer().getPluginManager().getPlugin("WorldEdit") == null) {
            return false;
        }
        RegisteredServiceProvider<WorldEditPlugin> wep = getServer().getServicesManager().getRegistration(WorldEditPlugin.class);
        if (wep == null) {
            return false;
        }
        worldEdit = wep.getProvider();
        return worldEdit != null;
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
    
    
    /*
    private void setupVault(PluginManager pm) {
        Plugin vault = pm.getPlugin("Vault");
        if (vault != null && vault.isEnabled()) {
            setupEconomy();
        } else {
            this.getLogger().info("Vault not loaded: no economy command costs & no permission group support");
        }
    }
    
    */

    
    
    // this code was taken from KingFaris11
    public boolean saveInventoryToFile(Inventory inventory, File path, String fileName) {
        if (inventory == null || path == null || fileName == null) return false;
        try {
            File invFile = new File(path, fileName + ".invsave");
            if (invFile.exists()) invFile.delete();
            FileConfiguration invConfig = YamlConfiguration.loadConfiguration(invFile);

            invConfig.set("Title", inventory.getTitle());
            invConfig.set("Size", inventory.getSize());
            invConfig.set("Max stack size", inventory.getMaxStackSize());
            if (inventory.getHolder() instanceof Player) invConfig.set("Holder", ((Player) inventory.getHolder()).getName());
            ItemStack[] invContents = inventory.getContents();
            for (int i = 0; i < invContents.length; i++) {
                ItemStack itemInInv = invContents[i];
                if (itemInInv != null) if (itemInInv.getType() != Material.AIR) invConfig.set("Slot " + i, itemInInv);
            }

            invConfig.save(invFile);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
 // this code was taken from KingFaris11
    public Inventory getInventoryFromFile(File file, Player player) {
        if (file == null) return null;
        if (!file.exists() || file.isDirectory() || !file.getAbsolutePath().endsWith(".invsave")) return null;
        try {
            FileConfiguration invConfig = YamlConfiguration.loadConfiguration(file);
            Inventory inventory = null;
            String invTitle = invConfig.getString("Title", "Inventory");
            int invSize = invConfig.getInt("Size", 27);
            int invMaxStackSize = invConfig.getInt("Max stack size", 64);
            InventoryHolder invHolder = null;
            if (invConfig.contains("Holder")) invHolder = Bukkit.getPlayer(invConfig.getString("Holder"));
            inventory = Bukkit.getServer().createInventory(invHolder, invSize, ChatColor.translateAlternateColorCodes('&', invTitle));
            inventory.setMaxStackSize(invMaxStackSize);
            try {
                ItemStack[] invContents = new ItemStack[invSize];
                for (int i = 0; i < invSize; i++) {
                    if (invConfig.contains("Slot " + i)) invContents[i] = invConfig.getItemStack("Slot " + i);
                    else invContents[i] = new ItemStack(Material.AIR);
                }
                player.getInventory().setContents(invContents);
            } catch (Exception ex) {
            }
            try{
                ItemStack[] invContents = new ItemStack[invSize];
                for (int i = 0; i < invSize; i++) {
                    if (invConfig.contains("Armor " + i)) invContents[i] = invConfig.getItemStack("Armor " + i);
                    else invContents[i] = new ItemStack(Material.AIR);
                }
            } catch (Exception ex) {
            }
            
            return inventory;
        } catch (Exception ex) {
            return null;
        }
    }


}
