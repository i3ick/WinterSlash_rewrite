package me.i3ick.com;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import commands.Joingame;

public class WinterSlashMain extends JavaPlugin{

	
	
	// ARRRAY
		 public ArrayList<String> frozen = new ArrayList<String>();
		 public ArrayList<String> frozenred = new ArrayList<String>();
		 public ArrayList<String> frozengreen = new ArrayList<String>();
		 public ArrayList<String> ftag = new ArrayList<String>();
		 public ArrayList<String> beaconlist = new ArrayList<String>();

		 
		 public HashMap<String, Player> wsplayersHM = new HashMap<String, Player>();
		 public HashMap<String, Player> wsred = new HashMap<String, Player>();
		 public HashMap<String, Player> wsgreen = new HashMap<String, Player>();
		 Map<String,ArrayList<Player>> wsredmap = new HashMap<String,ArrayList<Player>>();
		 Map<String,ArrayList<String>> wsgreenmap = new HashMap<>();
		 
		 

   // VARIABLE
		 
		 private static WinterSlashMain main;
		 

	 public static JavaPlugin INSTANCE;
 	 public static JavaPlugin getInstance() {
 	 	return INSTANCE;
      }
 	 {
 	 INSTANCE = this;
  	 }
		 
 	 

  // ENABLE DISABLE
 	 
 	@Override
 	public void onDisable() {
 	
 		getLogger().info("WinterSlash Plugin Disabled!");
 	
 	}
 	
 	
 	
 	@Override
 	public void onEnable() {
 		
 		//ready config
 		final FileConfiguration config = this.getConfig();
 		
 		// we assign "main" variable a value
 				this.main = this;
 		
 		//load world
 		File configFile = new File(getDataFolder(), "config.yml");
 		   if(!configFile.exists())
 		   {
 			   this.getConfig().addDefault("Redspawn" + ".X", null);
 	           this.getConfig().addDefault("Worlds" + ".World", null);
 	           this.getConfig().addDefault("Greenspawn" + ".X", null);
 	           this.getConfig().addDefault("Lobby" + ".X", null);
 	           this.getConfig().addDefault("arenas" + ".X", null);
 	           this.getConfig().addDefault("PlayerData" + ".X", null);
 	           this.getConfig().addDefault("DeathPosition" + ".X", null);
 	           this.getConfig().addDefault("MinPlayerNumber" + ".X", null);

 	           this.getConfig().options().copyDefaults(true);
 	           this.saveConfig();
 		    saveDefaultConfig();
 		   }
 		   String playerWorld = this.getConfig().getString("Worlds" + ".World" );
 		   WorldCreator c = new WorldCreator(playerWorld);
 			c.createWorld();
 		
 		getLogger().info("WinterSlash: Worldname: " + playerWorld);

 		
 		// register commands
 		
 		this.getCommand("ws").setExecutor(new MainCommand(this));
 		
 		
 		//register events
 		this.getServer().getPluginManager().registerEvents(new WinterSlashEvents(this), this);
	
 		WinterSlashManager.getInstance().loadArenas();
 		WinterSlashManager.getInstance().setup();
 	
 		getLogger().info("Plugin Enabled!");
 		}
 	
 	
 	// RETURN BOOLEANS
 	
	public static boolean isInt(String sender) {
	    try {
	        Integer.parseInt(sender);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public static boolean isInventoryEmpty(Player p){
		for(ItemStack item : p.getInventory().getContents())
		{
		if(item != null)
		return true;
		}
		return false;
		}
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args){
		Player player = (Player) sender;
		Player[] onlinep = Bukkit.getServer().getOnlinePlayers();
		
		
		
		
		
		return false;
	}
 	
}
