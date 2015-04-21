package me.i3ick.winterslash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class WinterSlashEvents implements Listener{
    
    private WinterSlashMain plugin;
    private WinterSlashGameController gameController;
    
    public WinterSlashEvents(WinterSlashMain PassPlug, WinterSlashGameController PassPlug2){
        this.plugin = PassPlug;
        this.gameController = PassPlug2;
    }
    
    


    

    //Keeps frozen players still - Disable movement
 
/*    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {

        
        for (String arenas: gameController.arenaNameList){
            WinterSlashArena arena = gameController.getArena(arenas);
            if(arena == null){
                return;
            }
            PotionEffect potionEffect1 = new PotionEffect(PotionEffectType.SLOW, 24000, 100); 
            PotionEffect potionEffect2 = new PotionEffect(PotionEffectType.JUMP, 24000, -100); 
            if(arena.getPlayers().contains(e.getPlayer().getName())){
                if (!(arena.getUnfrozen().contains(e.getPlayer().getName()))) {
                    e.setCancelled(true);
         }
         return;
        }
            }
        } */
    
    /**
     * Care Package GUI logic
     * @param p
     */
    public void openCareGUI(Player p){
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.YELLOW + "Pick your care drop!");
        ItemStack decoy = new ItemStack(Material.CHEST);
        ItemMeta decoymeta = decoy.getItemMeta();
        decoymeta.setDisplayName(ChatColor.GREEN + "Decoy drop package!");
        decoy.setItemMeta(decoymeta);
        inv.setItem(1, decoy);
        
        p.openInventory(inv);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e){
        if(!ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Pick your care drop!"))
                return;
        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true);
        WinterSlashClasses classes = new WinterSlashClasses();
        Inventory inv = e.getInventory();
            
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem().hasItemMeta()){
            p.closeInventory(); return;
        }
        

        
        switch(e.getCurrentItem().getType()){
        case CHEST:
            classes.decoyPackage(p);
            p.closeInventory();
            break;
        }
    }
    
    
    /**
     * Makes trapped chests explode on interaction
     * with players
     * @param e
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void chestExplode(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        for (String arenas: gameController.arenaNameList) {
            WinterSlashArena arena = gameController.getArena(arenas);
            if(arena.getPlayers().contains(p.getName())){         
                if (e.getInventory().getHolder() instanceof Chest){
                    Chest ch = (Chest) e.getInventory().getHolder();
                    if(ch.getType().equals(Material.TRAPPED_CHEST)){
                        e.setCancelled(true);
                        if(arena.getRedTeam().contains(p.getName()) && ch.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.REDSTONE_BLOCK)){
                            p.sendMessage(ChatColor.AQUA + "This is a decoy!");
                        }
                        else if(arena.getGreenTeam().contains(p.getName()) && ch.getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.EMERALD_BLOCK)){
                            p.sendMessage(ChatColor.AQUA + "This is a decoy!");
                        }
                        else{
                            p.getWorld().createExplosion(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 3F, false, false);
                            ch.getBlock().setType(Material.AIR);
                            ch.getBlock().getRelative(BlockFace.DOWN).setType(gameController.blockRestore.get(ch.getBlock().getLocation()));
                            Block upd = ch.getBlock().getRelative(BlockFace.DOWN);
                            upd.getState().update();
                        }
                    }
                    // open inventory if it's a real care package
                    else{
                        openCareGUI(p);
                        e.setCancelled(true);
                        ch.getBlock().setType(Material.AIR);
                        ch.getBlock().getRelative(BlockFace.DOWN).setType(gameController.blockRestore.get(ch.getBlock().getLocation()));
                        Block upd = ch.getBlock().getRelative(BlockFace.DOWN);
                        int byt = gameController.blockRestoreByte.get(ch.getBlock().getLocation());
                        upd.setData((byte)byt);
                    }

                    }
            }
        }
    }
    
    
    public void explosionDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            for (String arenas: gameController.arenaNameList) {
                WinterSlashArena arena = gameController.getArena(arenas);
                if(arena.getPlayers().contains(player.getName())){         
                    Location DeathLoc = player.getLocation(); 
                    arena.setDeathData(player, DeathLoc);
                }
            }
        }
    }
    

    /**
     * Reviving tool logic
     * @param e
     */
    @EventHandler
    public void ff(EntityDamageByEntityEvent e){
        Entity victim_entity = e.getEntity();
        Entity damager_entity = e.getDamager();
        
        
        
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            for (String arenas: gameController.arenaNameList) {
                WinterSlashArena arena = gameController.getArena(arenas);
                if(arena.getPlayers().contains(player.getName())){         
                    Location DeathLoc = player.getLocation(); 
                    arena.setDeathData(player, DeathLoc);
                }
            }
        }
        
        if (victim_entity instanceof Player) {
            if (damager_entity instanceof Player) {
                Player victim = (Player) victim_entity;
                Player damager = (Player) damager_entity;
                
                
                for (String arenas: gameController.arenaNameList) {
                    WinterSlashArena arena = gameController.getArena(arenas);
                    if((arena.getGamers().contains(damager.getName()))){
                        if(arena.disableFire.contains(damager.getName())){
                            e.setCancelled(true);
                        }
                        
                        if(arena.getRedTeam().contains(damager.getName())){
                            
                            if(arena.getRedFrozen().contains(damager.getName())){
                                e.setCancelled(true);
                                return;
                            }
                            
                            if(arena.getRedTeam().contains(victim.getName())){
                                if(!(arena.getRedFrozen().contains(victim.getName()))){
                                    e.setCancelled(true);
                                }
                            }
                            
                            if(arena.getGreenTeam().contains(victim.getName())){
                                if((arena.getGreenFrozen().contains(victim.getName()))){
                                    e.setCancelled(true);
                                }
                            }
                            return;
                        }
                        
                        
                        if(arena.getGreenTeam().contains(damager.getName())){
                            
                            if(arena.getGreenFrozen().contains(damager.getName())){
                                e.setCancelled(true);
                                return;
                            }
                            
                            if(arena.getGreenTeam().contains(victim.getName())){

                                if(!(arena.getGreenFrozen().contains(victim.getName()))){
                                    e.setCancelled(true);
                                }
                            }
                                
                                if(arena.getRedTeam().contains(victim.getName())){
                                    if((arena.getRedFrozen().contains(victim.getName()))){
                                        e.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    
    
   /**
    * Detect drop markers and deliver killstreaks
    * @param bl
    */
    @EventHandler
    public void placement(BlockPlaceEvent bl) {
        Player p = bl.getPlayer();

        for (String arenas : gameController.arenaNameList) {
            WinterSlashArena arena = gameController.getArena(arenas);
            WinterSlashClasses classes = new WinterSlashClasses();
            if ((arena.getGamers().contains(p.getName()))) {
                ItemMeta im = p.getItemInHand().getItemMeta();
                if (p.getItemInHand().hasItemMeta()){
                if (im.getDisplayName().equals(ChatColor.RED + "CarePackage drop marker")) {
                    Location pos = bl.getBlockPlaced().getLocation();
                    Location target = bl.getBlockPlaced().getRelative(BlockFace.DOWN).getLocation();
                    Block targetBlock = bl.getBlockPlaced().getRelative(BlockFace.DOWN);

                    // block invalid placements
                    if(targetBlock.getType().equals(Material.CHEST) || targetBlock.getType().equals(Material.REDSTONE_BLOCK) ||
                            targetBlock.getType().equals(Material.EMERALD_BLOCK) || targetBlock.getType().equals(Material.TRAPPED_CHEST)
                            || targetBlock.getRelative(BlockFace.EAST).getType().equals(Material.REDSTONE_BLOCK) ||
                            targetBlock.getRelative(BlockFace.NORTH).getType().equals(Material.REDSTONE_BLOCK)
                            || targetBlock.getRelative(BlockFace.SOUTH).getType().equals(Material.REDSTONE_BLOCK)
                            || targetBlock.getRelative(BlockFace.WEST).getType().equals(Material.REDSTONE_BLOCK)
                            || targetBlock.getRelative(BlockFace.EAST).getType().equals(Material.EMERALD_BLOCK) ||
                            targetBlock.getRelative(BlockFace.NORTH).getType().equals(Material.EMERALD_BLOCK)
                            || targetBlock.getRelative(BlockFace.SOUTH).getType().equals(Material.EMERALD_BLOCK)
                            || targetBlock.getRelative(BlockFace.WEST).getType().equals(Material.EMERALD_BLOCK)){
                        bl.setCancelled(true);
                        p.sendMessage(ChatColor.YELLOW + "Can't mark here.");
                        return;
                    }

                    p.getWorld().getBlockAt(pos).setType(Material.AIR);
                    Material b = bl.getBlockPlaced().getRelative(BlockFace.DOWN).getType();
                    gameController.blockRestore.put(pos, bl.getBlockPlaced().getRelative(BlockFace.DOWN).getType());
                    gameController.blockRestoreByte.put(pos, bl.getBlockPlaced().getRelative(BlockFace.DOWN).getData());
                    gameController.userBlockLoc.put(p.getName(), pos);
                    if(arena.getRedTeam().contains(p.getName())){
                        p.getWorld().getBlockAt(target).setType(Material.REDSTONE_BLOCK);
                    }
                    if(arena.getGreenTeam().contains(p.getName())){
                        p.getWorld().getBlockAt(target).setType(Material.EMERALD_BLOCK);
                    }
                    classes.signalCare(p, pos);
                    careWait(p, pos, target, b);
                    p.sendMessage(ChatColor.AQUA + "Care Dropping in 3 seconds!");
                    return;
                    }
                
                if (im.getDisplayName().equals(ChatColor.RED + "Decoy drop marker")) {
                    Location pos = bl.getBlockPlaced().getLocation();
                    Location target = bl.getBlockPlaced().getRelative(BlockFace.DOWN).getLocation();
                    Block targetBlock = bl.getBlockPlaced().getRelative(BlockFace.DOWN);

                    // block invalid placements
                    if(targetBlock.getType().equals(Material.CHEST) || targetBlock.getType().equals(Material.REDSTONE_BLOCK) ||
                            targetBlock.getType().equals(Material.EMERALD_BLOCK) || targetBlock.getType().equals(Material.TRAPPED_CHEST)
                            || targetBlock.getRelative(BlockFace.EAST).getType().equals(Material.REDSTONE_BLOCK) ||
                            targetBlock.getRelative(BlockFace.NORTH).getType().equals(Material.REDSTONE_BLOCK)
                            || targetBlock.getRelative(BlockFace.SOUTH).getType().equals(Material.REDSTONE_BLOCK)
                            || targetBlock.getRelative(BlockFace.WEST).getType().equals(Material.REDSTONE_BLOCK)
                            || targetBlock.getRelative(BlockFace.EAST).getType().equals(Material.EMERALD_BLOCK) ||
                            targetBlock.getRelative(BlockFace.NORTH).getType().equals(Material.EMERALD_BLOCK)
                            || targetBlock.getRelative(BlockFace.SOUTH).getType().equals(Material.EMERALD_BLOCK)
                            || targetBlock.getRelative(BlockFace.WEST).getType().equals(Material.EMERALD_BLOCK)){
                        bl.setCancelled(true);
                        p.sendMessage(ChatColor.YELLOW + "Can't mark here.");
                        return;
                    }

                    p.getWorld().getBlockAt(pos).setType(Material.AIR);
                    Material b = bl.getBlockPlaced().getRelative(BlockFace.DOWN).getType();
                    gameController.blockRestore.put(pos, bl.getBlockPlaced().getRelative(BlockFace.DOWN).getType());
                    gameController.blockRestoreByte.put(pos, bl.getBlockPlaced().getRelative(BlockFace.DOWN).getData());
                    gameController.userBlockLoc.put(p.getName(), pos);
                    if(arena.getRedTeam().contains(p.getName())){
                        p.getWorld().getBlockAt(target).setType(Material.REDSTONE_BLOCK);
                    }
                    if(arena.getGreenTeam().contains(p.getName())){
                        p.getWorld().getBlockAt(target).setType(Material.EMERALD_BLOCK);
                    }
                    classes.signalCare(p, pos);
                    decoyCareWait(p, pos, target, b);
                    p.sendMessage(ChatColor.AQUA + "Decoy Dropping in 3 seconds!");
                    return;
                    }
                }
            }
        }
    }
    
    
    /**
     * Disable Pickups
     * @param e
     */
    @EventHandler
    public void pickup(PlayerPickupItemEvent e){
        Player p = (Player) e.getPlayer();
        WinterSlashPlayerInfo info = new WinterSlashPlayerInfo(gameController);
        if(!info.isInGame()){
            return;
        }
        e.setCancelled(true);
    }

    /**
     * Cancel item dropping
     * @param e
     */
    @EventHandler
    public void drops(PlayerDropItemEvent e) {
        Player p = (Player) e.getPlayer();
        WinterSlashPlayerInfo info = new WinterSlashPlayerInfo(gameController);
        if(!info.isInGame()){
            return;
        }
        e.setCancelled(true);

    }
    

    
    /**
     * Game winning logic
     * @param e
     */
    
      //Game ending & freezing/unfreezing logic
      @EventHandler(priority = EventPriority.HIGHEST)
      public void checkDeath(PlayerDeathEvent e) {
          Entity entity = e.getEntity();
          Player p;
          Player killer;
          p = (Player) entity;
          killer = p.getKiller();


        // GAME WINNING LOGIC  
          
          for (String arenas: gameController.arenaNameList) {
              WinterSlashArena arena = gameController.getArena(arenas);

              
              if(arena.getAlive().contains(p.getName())){
                  arena.getAlive().remove(p.getName());
              }
              
              if(arena.getPlayers().contains(p.getName())){
                  

              
                  if (arena.getRedTeam().contains(p.getName())) {
                      //killstreaks
                      
                      
                      // On Wolf Kill
                      if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                          EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();
               
                          if ((nEvent.getDamager() instanceof Wolf)) {
                              gameController.getPlayerData(p.getName()).clearKillstreak(p);
                              Wolf we = (Wolf) nEvent.getDamager();
                              Player wolfOwner = (Player) we.getOwner();
                              gameController.getPlayerData(wolfOwner.getName()).addKill(wolfOwner);
                              KillStreakLogic killLogic = new KillStreakLogic(gameController, plugin);
                              

                              for(String alivePlayer : arena.getAlive()){
                                  if(arena.getRedTeam().contains(alivePlayer)){
                                      Player enemy = Bukkit.getServer().getPlayer(alivePlayer);
                                      killLogic.changeTarget(wolfOwner.getName(), enemy);
                                      
                                  }
                              }
                          }
                      }
                      
                      // On Player Kill
                         if ((killer instanceof Player)) {
                             gameController.getPlayerData(killer.getName()).addKill(killer);
                             gameController.getPlayerData(p.getName()).clearKillstreak(p);
                             
                             KillStreakLogic killLogic = new KillStreakLogic(gameController, plugin);
                             killLogic.killLogic(killer, arena);
                         }
                      
                      // if player isn't frozen, freeze him. If he is, unfreeze him.
                      if (arena.getUnfrozen().contains(p.getName())) {
                          arena.setFrozen(p.getName());
                          arena.addRedFrozen(p.getName());
                      }
                      else{
                          arena.setUnfrozen(p.getName());
                          arena.removeRedFrozen(p.getName());
                      }
                  } else if ((arena.getGreenTeam().contains(p.getName()))) {
                      
                      
                      // On Wolf Kill
                      if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                          EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();
               
                          if ((nEvent.getDamager() instanceof Wolf)) {
                              gameController.getPlayerData(p.getName()).clearKillstreak(p);
                              Wolf we = (Wolf) nEvent.getDamager();
                              Player wolfOwner = (Player) we.getOwner();
                              gameController.getPlayerData(wolfOwner.getName()).addKill(wolfOwner);
                              KillStreakLogic killLogic = new KillStreakLogic(gameController, plugin);
                              

                              for(String alivePlayer : arena.getAlive()){
                                  if(arena.getGreenTeam().contains(alivePlayer)){
                                      Player enemy = Bukkit.getServer().getPlayer(alivePlayer);
                                      killLogic.changeTarget(wolfOwner.getName(), enemy);
                                      
                                  }
                              }
                          }
                      }
                      
                      // On Player Kill
                         if ((killer instanceof Player)) {
                             gameController.getPlayerData(killer.getName()).addKill(killer);
                             gameController.getPlayerData(p.getName()).clearKillstreak(p);
                             
                             KillStreakLogic killLogic = new KillStreakLogic(gameController, plugin);
                             killLogic.killLogic(killer, arena);
                         }                   
                      
                   // if player isn't frozen, freeze him. If he is, unfreeze him.
                      
                      if (arena.getUnfrozen().contains(p.getName())) {
                          arena.setFrozen(p.getName());
                          arena.addGreenFrozen(p.getName());
                      }
                      else{
                          arena.setUnfrozen(p.getName());
                          arena.removeGreenFrozen(p.getName());
                      }
                  }
              
                  // End game if green wins
                  if (arena.getGreenFrozen().size() == arena.getGreenTeam().size()){
                      //  end the game...
                      arena.sendMessage(ChatColor.GREEN + "The RED team has won the game!");
                      for(String b: arena.getGreenTeam()){
                          Player pl = Bukkit.getPlayer(b);
                          if(!(arena.getAlive().contains(pl.getName()))){
                              gameController.addRestore(pl.getName());
                          }
                          
                      }
                      for(String a: arena.getRedTeam()){
                          Player pl = Bukkit.getPlayer(a);
                          if(arena.getAlive().contains(pl.getName())){
                              gameController.awardMoney(pl);
                          }else{
                          gameController.Winner.add(a);
                          }
                          theEnd(pl, arenas);
                      }
                      return;
                  }
                  
                  // End game if red wins
                  if (arena.getRedFrozen().size() == arena.getRedTeam().size()) {
                      //  end the game...
                      for(String r: arena.getAlive()){
                          Player pe = Bukkit.getPlayer(r);
                          if(!(arena.getAlive().contains(pe.getName()))){
                              gameController.addRestore(pe.getName());
                          }
                      }
                      gameController.addRestore(p.getName());
                      arena.sendMessage(ChatColor.GREEN + "The GREEN team has won the game!");
                      for(String b: arena.getRedTeam()){
                          Player pl = Bukkit.getPlayer(b);
                          if(!(arena.getAlive().contains(pl.getName()))){
                              gameController.addRestore(pl.getName());
                          }
                          
                      }
                      for(String a: arena.getGreenTeam()){
                          Player pl = Bukkit.getPlayer(a);
                          if(arena.getAlive().contains(pl.getName())){
                              gameController.awardMoney(pl);
                          }else{
                          gameController.Winner.add(a);
                          }
                          theEnd(pl, arenas);
                      }
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
                  gameController.forceRemovePlayers(player, arena.getName());
                  if(arena.getRedTeam().size() == 0 | arena.getGreenTeam().size() == 0){
                      gameController.endKick(arena.getName());
                      gameController.endArena(arena.getName());
                  return;
                  }
              }
          }
      }
      
    /*  @EventHandler
      public void onConnect(PlayerJoinEvent e){
          Player p = e.getPlayer(); 
          if(gameController.PlayerArmor.containsKey(p.getName())){ 
          slowConnect(p);
          }
      } */
      
      
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
           
           for (String arenas: gameController.arenaNameList) {
              WinterSlashArena arena = gameController.getArena(arenas);
              if(arena.getPlayers().contains(p.getName())){
                  arena.getAlive().add(p.getName());
                  
                  if(gameController.restorePlayers.contains(p.getName())){
                      if(gameController.Winner.contains(p)){
                          gameController.awardMoney(p);
                          gameController.Winner.remove(p);
                      }
                      gameController.removeDeadPlayers(p, arena.getName());
                      gameController.playersInGame.remove(p.getName());
                      runDelayTeleportInit(p);
                      return;
                }
                  
                  
                  //if p is red
                   if (arena.getRedTeam().contains(p.getName())) {
              if (arena.getUnfrozen().contains(p.getName())){
                  e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR,1));
                  WinterSlashClasses classes = new WinterSlashClasses();
                  classes.redArmor(p);
                  runDelayTeleportRed(p);
                  if(gameController.Light.contains(p)){
                      classes.setRunner(p);
                      return;
                  }
                  
                  if(gameController.Heavy.contains(p)){
                      classes.setHeavy(p);
                      return;
                  }

                  if(gameController.Archer.contains(p)){
                      classes.setArcher(p);
                      return;
                  }
                  classes.setDefault(p);
              }
              else{
                  WinterSlashClasses classes = new WinterSlashClasses();
                  classes.redFrozenArmor(p);
                  e.getPlayer().getInventory().setHelmet(new ItemStack(Material.ICE,1));
                  runDelayTeleport(p);
                  
                  potionDelay(p);
                    }
                }



                 //if p is green
                  else if(arena.getGreenTeam().contains(p.getName())) {
              if (arena.getUnfrozen().contains(p.getName())){
                  e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR,1));
                  WinterSlashClasses classes = new WinterSlashClasses();
                  classes.greenArmor(p);
                  runDelayTeleportGreen(p);
                  if(gameController.Light.contains(p)){
                      classes.setRunner(p);
                      return;
                  }
                  
                  if(gameController.Heavy.contains(p)){
                      classes.setHeavy(p);
                      return;
                  }

                  if(gameController.Archer.contains(p)){
                      classes.setArcher(p);
                      return;
                  }
                  classes.setDefault(p);
                  
              }
              else{
                  WinterSlashClasses classes = new WinterSlashClasses();
                  classes.greenFrozenArmor(p);
                  e.getPlayer().getInventory().setHelmet(new ItemStack(Material.ICE,1));
                  runDelayTeleport(p);
                  
                  potionDelay(p);
                    }
                }
                   
                   
                   if(arena.getPlayers().isEmpty()){
                       gameController.endArena(arena.getName());
                   }
            }
        }
    }


      
      
      public void potionDelay(final Player p) {
          BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
          scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
              @Override
              public void run() {
                  for (String arenas: gameController.arenaNameList) {
                      WinterSlashArena arena = gameController.getArena(arenas);
                      if(arena.getGamers().contains(p.getName())){
                          PotionEffect potionEffect1 = new PotionEffect(PotionEffectType.SLOW, 24000, 6); 
                          PotionEffect potionEffect2 = new PotionEffect(PotionEffectType.JUMP, 24000, -10); 
                          p.addPotionEffect(potionEffect1);
                          p.addPotionEffect(potionEffect2);
                          
                    }
                }
            }
        }, 23L);
    }
      
      public void runDelayTeleport(final Player player) {
          BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
          scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
              @Override
              public void run() {
                  for (String arenas: gameController.arenaNameList) {
                      WinterSlashArena arena = gameController.getArena(arenas);
                      if(arena.getGamers().contains(player.getName())){
                          Bukkit.getPlayer(player.getUniqueId()).teleport(arena.getDeathData(player));
                          
                    }
                }
            }
        }, 20L);
    }
      
      
      public void runDelayTeleportInit(final Player player) {
          BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
          scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
              @Override
              public void run() {
                          Bukkit.getPlayer(player.getUniqueId()).teleport(gameController.getInitData(player));
            }
        }, 20L);
    }
      
      public void slowConnect(final Player player) {
          BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
          scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
              @Override
              public void run() {
                  player.getInventory().clear();
                  ItemStack nothing = new ItemStack(Material.AIR, 1);
                  player.getInventory().setHelmet(nothing);
                  player.getInventory().setChestplate(nothing);
                  player.getInventory().setLeggings(nothing);
                  player.getInventory().setBoots(nothing);
                  player.getInventory().setArmorContents(gameController.PlayerArmor.get(player));             
                  plugin.getInventoryFromFile(new File(plugin.getDataFolder(), player.getName() + ".invsave"), player);
                  gameController.playersInGame.remove(player.getName());
                  Bukkit.getPlayer(player.getUniqueId()).teleport(gameController.getInitData(player));
            }
        }, 60L);
    }
      
      public void theEnd(final Player player,final String arenas) {
          BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
          scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
              @Override
              public void run() {
                  WinterSlashArena arena = gameController.getArena(arenas);
                  gameController.endKick(arena.getName());
                  gameController.removePlayers(player, arenas);

                          
            }
        }, 20L);
    }
      
      
      public void runDelayTeleportRed(final Player player) {
          BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
          scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
              @Override
              public void run() {
                  for (String arenas: gameController.arenaNameList) {
                      WinterSlashArena arena = gameController.getArena(arenas);
                      if(arena.getGamers().contains(player.getName())){
                          Bukkit.getPlayer(player.getUniqueId()).teleport(arena.getRedSpawn());
                      }
                  }
            }
        }, 20L);
    }
      
      public void careWait(final Player player, final Location pos, final Location pos2, final Material block) {
          BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
          scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
              @Override
              public void run() {
                  WinterSlashClasses classes = new WinterSlashClasses();
                  classes.getCare(player, pos);
            }
        }, 60L);
    }
      
      
      public void decoyCareWait(final Player player, final Location pos, final Location pos2, final Material block) {
          BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
          scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
              @Override
              public void run() {
                  WinterSlashClasses classes = new WinterSlashClasses();
                  classes.getDecoy(player, pos);
            }
        }, 60L);
    }
      
      public void runDelayTeleportGreen(final Player player) {
          BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
          scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
              @Override
              public void run() {
                  for (String arenas: gameController.arenaNameList) {
                      WinterSlashArena arena = gameController.getArena(arenas);
                      if(arena.getGamers().contains(player.getName())){
                          Bukkit.getPlayer(player.getUniqueId()).teleport(arena.getGreenSpawn());
                      }
                  }
            }
        }, 20L);
    }
}
