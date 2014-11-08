package me.i3ick.winterslash;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class WinterSlashEvents implements Listener{
    
    private WinterSlashMain plugin;
    private WinterSlashGameController gameController;
    
    public WinterSlashEvents(WinterSlashMain PassPlug){
        this.plugin = PassPlug;
    }
    
    public WinterSlashEvents(WinterSlashGameController PassPlug){
        this.gameController = PassPlug;
    }
    
    // Save death location
    
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
        
        ConfigurationSection sec = plugin.getArenaData().getConfigurationSection("arenas");
        if(sec == null){
            return;
        }
        for (String arenas: sec.getKeys(false)) {
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
          
          //Method for getting the arena name which contains specific player
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
          }
      }
    
    
    
    

}
