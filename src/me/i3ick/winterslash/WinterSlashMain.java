package me.i3ick.winterslash;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import me.i3ick.winterslash.commands.MainCommand;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class WinterSlashMain extends JavaPlugin {
    
    public static Economy econ = null;
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
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
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
        gameController.loadArenas();
        
        WinterSlashArenaCreator creator = new WinterSlashArenaCreator();
        
        
        // register commands
        this.getCommand("ws").setExecutor(new MainCommand(this, creator, gameController));
        

        // register events
        this.getServer().getPluginManager().registerEvents(new WinterSlashEvents(this, gameController), this);
        this.getServer().getPluginManager().registerEvents(new WinterSlashSigns(this, gameController), this);
        
        
        getLogger().info("WinterSlash Enabled!");
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

    public FileConfiguration getArenaData(){
        File f = new File(this.getDataFolder() + File.separator + "arenaData.yml");
        FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(f);
            return arenaConfig;
    }
    
    
    public void awardMoney(Player player){
        
        double a = 3.32;           
        EconomyResponse r = econ.depositPlayer(player, a);
        if(r.transactionSuccess()) {
            player.sendMessage(String.format(ChatColor.GREEN + "You were awarded %s for winning the round and now you have a total of %s", econ.format(r.amount), econ.format(r.balance)));
        } else {
            player.sendMessage(String.format("An error occured: %s", r.errorMessage));
        }
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
