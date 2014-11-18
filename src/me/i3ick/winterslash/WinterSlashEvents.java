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
import org.bukkit.scheduler.BukkitScheduler;

public class WinterSlashEvents implements Listener{
    
    private WinterSlashMain plugin;
    private WinterSlashGameController gameController;
    
    public WinterSlashEvents(WinterSlashMain PassPlug, WinterSlashGameController PassPlug2){
        this.plugin = PassPlug;
        this.gameController = PassPlug2;
    }
    
    
    /**
     * Method for saving death location
     * @param event
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            for (String arenas: gameController.arenaNameList) {
                WinterSlashArena arena = gameController.getArena(arenas);
                if(arena.getPlayers().contains(player.getName())){         
                    Location DeathLoc = player.getLocation(); 
                    arena.setDeathData(player, DeathLoc);
                    
                 return;
            }
        }
    }
    }

    

    //Keeps frozen players still - Disable movement
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {

        
        for (String arenas: gameController.arenaNameList){
            WinterSlashArena arena = gameController.getArena(arenas);
            if(arena == null){
                return;
            }
            
            if(arena.getPlayers().contains(e.getPlayer().getName())){
                if (!(arena.getUnfrozen().contains(e.getPlayer().getName()))) {
                    e.getPlayer().teleport(e.getPlayer().getLocation());  
         }
         return;
        }
            }
        }
    
    
    
    //prevent damage from frozen players / prevent friendly fire / prevent killing frozen players
    @EventHandler
    public void onPlayerDamageFrozenDamage(EntityDamageByEntityEvent event) {
        Entity victim_entity = event.getEntity();
        Entity damager_entity = event.getDamager();
        if (victim_entity instanceof Player) {
            if (damager_entity instanceof Player) {
                Player victim = (Player) victim_entity;
                Player damager = (Player) damager_entity;
                
                for (String arenas: gameController.arenaNameList) {
                    WinterSlashArena arena = gameController.getArena(arenas);
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
          
          for (String arenas: gameController.arenaNameList) {
              WinterSlashArena arena = gameController.getArena(arenas);
              if(arena.getPlayers().contains(p.getName())){
              
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
              
                  // End game if green wins
                  if (arena.getGreenFrozen().size() == arena.getGreenTeam().size()){
                      //  end the game...
                      Bukkit.broadcastMessage(ChatColor.GREEN + "The GREEN team has won the game!");
                      gameController.addRestore(p.getName());
                      gameController.endArena(arena.getName());
                      gameController.removePlayers(p, arenas);
                      return;
                  }
                  // End game if red wins
                  if (arena.getRedFrozen().size() == arena.getRedTeam().size()) {
                      //  end the game...
                      Bukkit.broadcastMessage(ChatColor.GREEN + "The RED team has won the game!");
                      gameController.addRestore(p.getName());
                      gameController.endArena(arena.getName());
                      gameController.removePlayers(p, arenas);
                      return;
                  } 
              }
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
          for(String arenas: gameController.arenaNameList){
              WinterSlashArena arena = gameController.getArena(arenas);
              if(arena.getPlayers().contains(player.getName())){
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
           for (String arenas: gameController.arenaNameList) {
              WinterSlashArena arena = gameController.getArena(arenas);
              if(arena == null){
                  return;
              }
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
          plugin.getLogger().info("1");
          
          for (String arenas: gameController.arenaNameList) {
              WinterSlashArena arena = gameController.getArena(arenas);
          
          if(gameController.restorePlayers.contains(p.getName())){
                  plugin.awardMoney(p);
                  gameController.removePlayers(p, arenas);
          }
          }
          
           
           for (String arenas: gameController.arenaNameList) {
               plugin.getLogger().info("3");
              WinterSlashArena arena = gameController.getArena(arenas);
              if(arena.getPlayers().contains(p.getName())){
                  plugin.getLogger().info("4");
                  arena.setUnfrozen(p.getName());
                  
                  
                  //if p is red
                   if (arena.ifPlayerIsRed(p)) {
                       plugin.getLogger().info("5");
              if (arena.getUnfrozen().contains(p.getName())){
                  e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR,1));
                  p.teleport(arena.getRedSpawn());
              }
              else{
                  e.getPlayer().getInventory().setHelmet(new ItemStack(Material.ICE,1));

                  //set armor
                //   classes.setarmor();
                  
                  if(gameController.restorePlayers.contains(p.getName())){
                      this.runDelayTeleport(p);
                      return;
              }
              }
          }



                 //if p is green
                  else if(!(arena.ifPlayerIsRed(p))) {
                      plugin.getLogger().info("6");
              if (arena.getUnfrozen().contains(p.getName())){
                  e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR,1));
                  p.teleport(arena.getGreenSpawn());
              }
              else{
                  e.getPlayer().getInventory().setHelmet(new ItemStack(Material.ICE,1));

                  //set armor
                 // classes.setarmor();
                  
                  
                  if(gameController.restorePlayers.contains(p.getName())){
                      this.runDelayTeleport(p);
                      return;
              }
              }
              }
                   return;
                   }
              else{
                  return;
              }
              }
           }
      

      public void runDelayTeleport(final Player player) {
          BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
          scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
              @Override
              public void run() {
                  for (String arenas: gameController.arenaNameList) {
                      if(arenas.contains(player.getName())){
                          WinterSlashArena arena = gameController.getArena(arenas);
                          player.teleport(arena.getDeathData(player));
                      }
                  }
              }
          }, 20L);
          }
}
