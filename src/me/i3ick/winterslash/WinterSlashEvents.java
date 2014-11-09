package me.i3ick.winterslash;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class WinterSlashEvents implements Listener{
    
    private WinterSlashMain plugin;
    private WinterSlashGameController gameController;
    
    public WinterSlashEvents(WinterSlashMain PassPlug){
        this.plugin = PassPlug;
    }
    
    public WinterSlashEvents(WinterSlashGameController PassPlug){
        this.gameController = PassPlug;
    }
    
    
    /**
     * Method for saving death location
     * @param event
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            FileConfiguration arenaData = plugin.getArenaData();
            if(arenaData == null){
                return;
            }
            ConfigurationSection sec = plugin.getArenaData().getConfigurationSection("arenas");
            for (String arenas: sec.getKeys(false)) {
                WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
                if(arena.getPlayers().contains(player.getName())){
                    player.getLocation();
                    arenaData.set("DeathPosition." + player.getUniqueId() + ".X", player.getLocation().getBlockX());
                    arenaData.set("DeathPosition." + player.getUniqueId() + ".Y", player.getLocation().getBlockY());
                    arenaData.set("DeathPosition." + player.getUniqueId()+ ".Z", player.getLocation().getBlockZ());
                    plugin.saveArenaData();
                 return;
            }
        }
    }
    }

    

    //Keeps frozen players still - Disable movement
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {
        
        ConfigurationSection arenaData = plugin.getArenaData().getConfigurationSection("arenas");
        if(arenaData == null){
            return;
        }
        for (String arenas: arenaData.getKeys(false)) {
            WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
            if(arena.getPlayers().contains(e.getPlayer().getUniqueId())){
        
    
         if (!(arena.getUnfrozen().contains(e.getPlayer().getUniqueId()))) {
           e.getPlayer().teleport(e.getPlayer().getLocation());  
         }
         return;
        }
            }
        }
    
    
    
    //prevent damage from frozen players / prevent friendly fire / prevent killing frozen players
    @EventHandler
    public void onPlayerDamage_frozen_damage(EntityDamageByEntityEvent event) {
        Entity victim_entity = event.getEntity();
        Entity damager_entity = event.getDamager();
        if (victim_entity instanceof Player) {
            if (damager_entity instanceof Player) {
                Player victim = (Player) victim_entity;
                Player damager = (Player) damager_entity;
                
                ConfigurationSection sec = plugin.getArenaData().getConfigurationSection("arenas");
                for (String arenas: sec.getKeys(false)) {
                    WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
                    if(arena.getPlayers().contains(victim.getName())){
                    
                    
           

                //disable FF for red team
                if (arena.ifPlayerIsRed(victim)) {
                    if (arena.getRedFrozen().contains(victim)) {
                        if (arena.getRedTeam().contains(damager.getName())) {
                            //do nothing - unfreezeing process
                        } else {
                            event.setCancelled(true);
                            //disables other people to harm frozen people
                        }
                    } else if (arena.getRedTeam().contains(damager.getName())) {
                        
                            event.setCancelled(true);
                            //disables friendly fire
                        }
                    }
                
                
                 //disable FF for green team
                else if (!(arena.ifPlayerIsRed(victim))) {
                    if (arena.getGreenFrozen().contains(victim)) {
                        if (arena.getGreenTeam().contains(damager.getName())) {
                            //do nothing - unfreezeing process
                        } else {
                            event.setCancelled(true);
                            //disables other people to harm frozen people
                        }
                    } else if(arena.getGreenTeam().contains(damager.getName())) {
                            event.setCancelled(true);
                            //disables friendly fire
                        }
                    }
                
                return;
                }
                }
            }
        }
     }

    
    
    //Add logic to prevent revivng tools from causing damage
    //Add logic to prevent revivng tools from causing damage
    //Add logic to prevent revivng tools from causing damage
    
    
      
      //Game ending & freezing/unfreezing logic
      @EventHandler(priority = EventPriority.HIGHEST)
      public void checkDeath(PlayerDeathEvent e) {
          Entity entity = e.getEntity();
          Player p;
          if (entity instanceof Player) {
              p = (Player) entity;
          } else {
              return;
          }

          

        // GAME WINNING LOGIC  
          
          ConfigurationSection arenaData = plugin.getArenaData().getConfigurationSection("arenas");
          if(arenaData == null){
              return;
          }
          for (String arenas: arenaData.getKeys(false)) {
              WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
              if(arena.getPlayers().contains(p.getName())){
                  arena.setFrozen(p.getName());
              
              
                  // End game if green wins
                  if (arena.getGreenFrozen().size() == arena.getGreenTeam().size()){
                      //  end the game...
                      Bukkit.broadcastMessage(ChatColor.GREEN + "The GREEN team has won the game!");
                      gameController.endArena(arena.getName());
                      return;
                  }
                  // End game if red wins
                  if (arena.getRedFrozen().size() == arena.getRedTeam().size()) {
                      //  end the game...
                      Bukkit.broadcastMessage(ChatColor.GREEN + "The RED team has won the game!");
                      gameController.endArena(arena.getName());
                      return;
                  } 
                  
                  
                  if (arena.ifPlayerIsRed(p)) {
                      // if player isn't frozen, freeze him. If he is, unfreeze him.
                      if (!arena.getUnfrozen().contains(p.getName())) {
                          arena.setFrozen(p.getName());
                          arena.addRedFrozen(p.getName());
                          plugin.getLogger().info("Debugger_freeze 1");
                      }
                      else{
                          arena.setUnfrozen(p.getName());
                          arena.removeRedFrozen(p.getName());
                      }
                  } else if (!(arena.ifPlayerIsRed(p))) {
                      if (!arena.getUnfrozen().contains(p.getName())) {
                          arena.setUnfrozen(p.getName());
                          arena.addGreenFrozen(p.getName());
                          plugin.getLogger().info("Debugger_freeze 2");
                      }
                      else{
                          arena.setFrozen(p.getName());
                          arena.removeGreenFrozen(p.getName());
                      }
                  }
                  return;
              }
              return;
          }
      }
     
      
      /**
       * removes the player from the game if
       * he disconnects
       * @param e
       */
      @EventHandler
      public void onDisconnect(PlayerQuitEvent e) {
          Player player = e.getPlayer();  
          for(String arenas: plugin.getArenaData().getConfigurationSection("arenas").getKeys(false)){
              WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
              if(arena.getGamers().contains(player)){
                  gameController.removePlayers(player, arena.getName());
              }
          }
      }
      
      
      
      /**
       * Blocks all commands for players that are ingame
       * @param e
       */
      @EventHandler(priority = EventPriority.HIGHEST)
      public void onServerCommand(PlayerCommandPreprocessEvent e) {
          ConfigurationSection arenaData = plugin.getArenaData().getConfigurationSection("arenas");
           if(arenaData == null){
              return;
          }
           for (String arenas: arenaData.getKeys(false)) {
              WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
              if(arena.getPlayers().contains(e.getPlayer().getName())){
              if (e.getMessage().equals("/ws leave")) {
                  return;
              } else {
                  if (e.getPlayer().isOp()) {
                      return;
                  } else {
                      e.setCancelled(true);
                      e.getPlayer().sendMessage(ChatColor.YELLOW + "Can't use commands while in game. Use '/ws leave' to leave");
                  }
              }
              return;
          } 
        }
      }
      
      
    
      /**
       * Handles respawn gameplay logic
       * freezing, teleporting, armor
       * @param e
       */
      
      @EventHandler (priority = EventPriority.HIGHEST)
      public void onPlayerRespawn(PlayerRespawnEvent e){
          Player p = e.getPlayer();
          ConfigurationSection arenaData = plugin.getArenaData().getConfigurationSection("arenas");
           if(arenaData == null){
              return;
          }
           
           for (String arenas: arenaData.getKeys(false)) {
               WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
           if(!(arena.getPlayers().contains(p.getName()))){
               return;
           }
           }
           
           for (String arenas: arenaData.getKeys(false)) {
              WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
              if(arena.getPlayers().contains(p.getName())){
                  arena.setUnfrozen(p.getName());
                  
                  
                  //if p is red
                   if (arena.ifPlayerIsRed(p)) {
              if (arena.getUnfrozen().contains(p.getName())){
                  e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR,1));
                  p.teleport(arena.getRedSpawn());
              }
              else{
                  e.getPlayer().getInventory().setHelmet(new ItemStack(Material.ICE,1));

                  //set armor
                //   classes.setarmor();
                  
                  
                  
                  // tp to death position
                  int lastposX = arenaData.getInt("DeathPosition." + p.getName() + ".X");
                  int lastposY = arenaData.getInt("DeathPosition." + p.getName() + ".Y");
                  int lastposZ = arenaData.getInt("DeathPosition." + p.getName() + ".Z");
                  String playerWorld = plugin.getConfig().getString("Worlds" + ".World");
                  String world = p.getLocation().getWorld().getName();

                  if(world != null)
                  {
                      Location lastpos = new Location((Bukkit.getWorld(world)), lastposX, lastposY, lastposZ);
                      e.setRespawnLocation(lastpos);
                  }
                  else
                  {
                      Bukkit.getServer().createWorld(new WorldCreator(playerWorld).environment(World.Environment.NORMAL));
                      plugin.getLogger().warning("The '" + "redspawn" + ".World" + "' world from config.yml does not exist or is not loaded !");
                  }   
              }
          }



                 //if p is green
                  else if(!(arena.ifPlayerIsRed(p))) {
              if (arena.getUnfrozen().contains(p.getName())){
                  e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR,1));
                  p.teleport(arena.getGreenSpawn());
              }
              else{
                  e.getPlayer().getInventory().setHelmet(new ItemStack(Material.ICE,1));

                  //set armor
                 // classes.setarmor();
                  
                  
                  
                  // tp to death position
                  int lastposX = arenaData.getInt("DeathPosition." + p.getName() + ".X");
                  int lastposY = arenaData.getInt("DeathPosition." + p.getName() + ".Y");
                  int lastposZ = arenaData.getInt("DeathPosition." + p.getName() + ".Z");
                  String playerWorld = arenaData.getString("Worlds" + ".World");
                  String world = p.getLocation().getWorld().getName();

                  if(world != null)
                  {
                      Location lastpos = new Location(Bukkit.getWorld(world), lastposX, lastposY, lastposZ);
                      p.teleport(lastpos);
                  }
                  else
                  {
                      Bukkit.getServer().createWorld(new WorldCreator(playerWorld).environment(World.Environment.NORMAL));
                      plugin.getLogger().warning("The '" + "redspawn" + ".World" + "' world from config.yml does not exist or is not loaded !");
                  }   
              }
              }
                   return;
                   }
              }
           }
      
      
    
    
    
    

}
