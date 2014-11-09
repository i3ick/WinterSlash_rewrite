package me.i3ick.winterslash;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WinterSlashSigns implements Listener{
    
    private WinterSlashMain plugin;
    private WinterSlashGameController gameController;
    
    public WinterSlashSigns(WinterSlashMain PassPlug, WinterSlashGameController PassPlug2){
        this.plugin = PassPlug;
        this.gameController = PassPlug2;
    }

   

    //Force start sign creation
    @EventHandler
    public void onSignCreation1(SignChangeEvent e) {
        Player player = (Player) e.getPlayer();
        if (e.getLine(0).equals("/forcestart")) {
            if (!player.hasPermission("winterslash.signplace")) {
                player.sendMessage("No permission");
                return;
            }
            String arenan = e.getLine(1).toString();
            ConfigurationSection arenaData = plugin.getArenaData().getConfigurationSection("arenas");
            for (String arenas: arenaData.getKeys(false)) {
                WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
                if(arena.getName().equals(arenan)){
                    e.setLine(0, ChatColor.YELLOW + ChatColor.BOLD.toString() + "Punch 2");
                    e.setLine(1, ChatColor.YELLOW + ChatColor.BOLD.toString() + "Force Start");
                    e.setLine(2, arenan);
                    e.setLine(3, ChatColor.MAGIC.toString() + ChatColor.BOLD.toString() + "stinky fish");
                    return;
                }
        }
        }
        }
    
    
    //Force start sign logic
      @EventHandler
      public void onForceStart(PlayerInteractEvent e) {
          Player player = (Player) e.getPlayer();
          
          if (!player.hasPermission("winterslash.join")) {
              player.sendMessage("No permission");
              return;}
          
          if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
              if (e.getClickedBlock().getType() == Material.SIGN_POST|| e.getClickedBlock().getType() == Material.WALL_SIGN) {
                  Sign sign = (Sign) e.getClickedBlock().getState();
                  if(sign.getLine(0).equalsIgnoreCase(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Punch 2")){
                   ConfigurationSection arenaData = plugin.getArenaData().getConfigurationSection("arenas");
                   for (String arenas: arenaData.getKeys(false)) {
                      WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
                      if(arena.getPlayers().contains(e.getPlayer().getName())){
                  
                  int playersLeft = plugin.getConfig().getInt(".MinPlayerNumber") - arena.getSign().size();
                  if(playersLeft < 1){
                      String arenan = sign.getLine(2).toString();
                      gameController.startArena(arenan);
                  }else{
                  arena.sendMessage(ChatColor.DARK_PURPLE.toString() + playersLeft + " more players needed to force start.");
                  }return;}
                      else{
                      }
                      }
              }
              }
          }
      }
      
      
      //Join sign creation
      @EventHandler
      public void onSignCreation2(SignChangeEvent e) {
          Player player = (Player) e.getPlayer();
          ConfigurationSection arenaData = plugin.getArenaData().getConfigurationSection("arenas");

          if (e.getLine(0).equals("/joinsign")) {
              if (!player.hasPermission("winterslash.signplace")) {
                  player.sendMessage("No permission");
                  return;
              }
              if(arenaData.contains(e.getLine(1))){
              String arenaName = e.getLine(1).toString();
              for (String arenas: arenaData.getKeys(false)) {
                  WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
                  if(arena.getName().equals(arenaName)){
                      e.setLine(0, ChatColor.BLUE + ChatColor.BOLD.toString() + "Winter Slash");
                      e.setLine(1, ChatColor.BLUE + ChatColor.BOLD.toString() + "join arena");
                      e.setLine(2, ChatColor.BLUE + arenaName);
                      return;
                  }
          }
          }else{player.sendMessage(ChatColor.RED + "No arena found with this name.");e.isCancelled();}
          }
          }
      
    
    //Join sign logic
    
      @EventHandler
      public void onJoinSign(PlayerInteractEvent e) {
          Player player = (Player) e.getPlayer();
          
          if (!player.hasPermission("winterslash.signplace")) {
              player.sendMessage("No permission");
              return;}
          
          if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
              if (e.getClickedBlock().getType() == Material.SIGN_POST|| e.getClickedBlock().getType() == Material.WALL_SIGN) {
                  Sign sign = (Sign) e.getClickedBlock().getState();
                  if(sign.getLine(0).equalsIgnoreCase(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Punch 2")){
                   ConfigurationSection arenaData = plugin.getArenaData().getConfigurationSection("arenas");
                   for (String arenas: arenaData.getKeys(false)) {
                      WinterSlashArena arena = WinterSlashGameController.getArena(arenas);
                      if(arena.getPlayers().contains(e.getPlayer().getName())){
                  
                  int playersLeft = plugin.getConfig().getInt(".MinPlayerNumber") - arena.getSign().size();
                  if(playersLeft < 1){
                      String arenan = sign.getLine(2).toString();
                      gameController.startArena(arenan);
                  }else{
                  arena.sendMessage(ChatColor.DARK_PURPLE.toString() + playersLeft + " more players needed to force start.");
                  }return;}
                      else{
                      }
                      }
              }
              }
          }
      }
      
      
      //Pack-a-Punch device
      
      @EventHandler
      public void onSignCreation3(SignChangeEvent e) {
          Player player = (Player) e.getPlayer();
          if (e.getLine(0).equals("/wspap")) {
              if (!player.hasPermission("winterslash.signplace")) {
                  player.sendMessage("No permission");
                  return;}
              e.setLine(0, ChatColor.BLUE + ChatColor.BOLD.toString() + "Pack");
              e.setLine(1, ChatColor.YELLOW + ChatColor.BOLD.toString() + "a");
              e.setLine(2, ChatColor.GREEN + ChatColor.BOLD.toString() + "Punch");
              e.setLine(3, ChatColor.MAGIC.toString() + ChatColor.BOLD.toString() + "fresh fish");
          }
      }
      
      //Pack-a-Punch logic
      @EventHandler
      public void onPaP(PlayerInteractEvent e) {
          Player player = (Player) e.getPlayer();
          
          if (!player.hasPermission("winterslash.signplace")) {
              player.sendMessage("No permission");
              return;}
          
          ItemStack revivor = new ItemStack(Material.BLAZE_ROD, 1);
          ItemStack revivorUpgrade = new ItemStack(Material.BONE, 1);
          int newItemSlot = player.getInventory().firstEmpty();
          if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
              if (e.getClickedBlock().getType() == Material.SIGN_POST|| e.getClickedBlock().getType() == Material.WALL_SIGN) {
                  Sign sign = (Sign) e.getClickedBlock().getState();
                  if(sign.getLine(0).equalsIgnoreCase(ChatColor.BLUE + ChatColor.BOLD.toString() + "Pack")){
                  revivorUpgrade.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
                  player.getInventory().setItem(newItemSlot, revivorUpgrade);
                  player.getInventory().removeItem(revivor);
                  }
              }
          }
      }
      
      
      
      
      
      
    
    
}
