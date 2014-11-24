package me.i3ick.winterslash;

import me.i3ick.winterslash.commands.Subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class WinterSlashSigns implements Listener{
    
    private WinterSlashMain plugin;
    private WinterSlashGameController gameController;
    private WinterSlashClasses classes;
    
    public WinterSlashSigns(WinterSlashMain PassPlug, WinterSlashGameController PassPlug2, WinterSlashClasses PassPlug3){
        this.plugin = PassPlug;
        this.gameController = PassPlug2;
        this.classes = PassPlug3;
    }

   

    // Sign creation
    @EventHandler
    public void onSignCreation1(SignChangeEvent e) {
        Player player = (Player) e.getPlayer();
        if (e.getLine(0).equals("/forcestart")) {
            
            String arenan = e.getLine(1).toString();

            for (String arenas : gameController.arenaNameList) {
                WinterSlashArena arena = gameController.getArena(arenas);
                if (arena.getName().equals(arenan)) {
                    e.setLine(0, ChatColor.YELLOW + ChatColor.BOLD.toString() + "Punch 2");
                    e.setLine(1, ChatColor.YELLOW + ChatColor.BOLD.toString() + "Force Start");
                    e.setLine(2, arenan);
                    e.setLine(3, ChatColor.MAGIC.toString() + ChatColor.BOLD.toString() + "stinky fish");
                    return;
                }
            }
        }

        if (e.getLine(0).equals("/joinsign")) {

            if (gameController.arenaNameList.contains(e.getLine(1))) {
                String arenaName = e.getLine(1).toString();
                for (String arenas : gameController.arenaNameList) {
                    WinterSlashArena arena = gameController.getArena(arenas);
                    if (arena.getName().equals(arenaName)) {
                        e.setLine(0, ChatColor.BLUE + ChatColor.BOLD.toString() + "WinterSlash");
                        e.setLine(1, ChatColor.BLUE + ChatColor.BOLD.toString() + "join arena");
                        e.setLine(2, arenaName);
                        return;
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "No arena found with this name.");
                e.isCancelled();
            }
        }

        if (e.getLine(0).equals("/wspap")) {

            e.setLine(0, ChatColor.BLUE + ChatColor.BOLD.toString() + "Pack");
            e.setLine(1, ChatColor.YELLOW + ChatColor.BOLD.toString() + "a");
            e.setLine(2, ChatColor.GREEN + ChatColor.BOLD.toString() + "Punch");
            e.setLine(3, ChatColor.MAGIC.toString() + ChatColor.BOLD.toString() + "fresh fish");
        }

        if (e.getLine(0).equals("/class heavy")) {

            e.setLine(0, ChatColor.BLUE + ChatColor.BOLD.toString() + "WinterSlash");
            e.setLine(1, ChatColor.RED + ChatColor.BOLD.toString() + "Heavy Class");
            return;
        }

        if (e.getLine(0).equals("/class light")) {

            e.setLine(0, ChatColor.BLUE + ChatColor.BOLD.toString() + "WinterSlash");
            e.setLine(1, ChatColor.RED + ChatColor.BOLD.toString() + "Light Class");
            return;
        }

        if (e.getLine(0).equals("/class recon")) {

            e.setLine(0, ChatColor.BLUE + ChatColor.BOLD.toString() + "WinterSlash");
            e.setLine(1, ChatColor.RED + ChatColor.BOLD.toString() + "Recon Class");
            return;
        }

    }
    
    
    //Sign logic
    
      @EventHandler
      public void onSignInteraction(PlayerInteractEvent e) {
          Player player = (Player) e.getPlayer();
          
          if (!player.hasPermission("winterslash.interact")) {
              player.sendMessage("No permission");
              return;}
          
          if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
              if (e.getClickedBlock().getType() == Material.SIGN_POST|| e.getClickedBlock().getType() == Material.WALL_SIGN) {
                  Sign sign = (Sign) e.getClickedBlock().getState();
                  if(sign.getLine(0).equalsIgnoreCase(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Punch 2")){
                   for (String arenas: gameController.arenaNameList) {
                      WinterSlashArena arena = gameController.getArena(arenas);
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
      
                  WinterSlashArenaCreator cr = new WinterSlashArenaCreator(plugin);
                  Subcommands subcmnds = new Subcommands(plugin, gameController, cr);
                  

                  String s = ChatColor.stripColor(sign.getLine(1));
                  if(s.equalsIgnoreCase("join arena")){            
                      subcmnds.join(player, sign.getLine(2).toString());
                      player.sendMessage(sign.getLine(2));
              }

      
                ItemStack revivor = new ItemStack(Material.BLAZE_ROD, 1);
                ItemStack revivorUpgrade = new ItemStack(Material.BONE, 1);
                int newItemSlot = player.getInventory().firstEmpty();
                if (sign.getLine(0).equalsIgnoreCase(
                        ChatColor.BLUE + ChatColor.BOLD.toString() + "Pack")) {
                    revivorUpgrade.addUnsafeEnchantment(Enchantment.DAMAGE_ALL,
                            6);
                    player.getInventory().setItem(newItemSlot, revivorUpgrade);
                    player.getInventory().removeItem(revivor);
                    player.updateInventory();
                }


                if (s.equalsIgnoreCase("Heavy Class")) {
                    if (gameController.Light.contains(player.getName())) {
                        gameController.Light.remove(player.getName());
                        player.getInventory().clear();
                        for(PotionEffect effect : player.getActivePotionEffects())
                        {
                            player.removePotionEffect(effect.getType());
                        }
                    }

                    if (gameController.Archer.contains(player.getName())) {
                        gameController.Archer.remove(player.getName());
                        player.getInventory().clear();
                        for(PotionEffect effect : player.getActivePotionEffects())
                        {
                            player.removePotionEffect(effect.getType());
                        }
                    }

                    if (gameController.Heavy.contains(player.getName())) {
                        player.sendMessage("You are a heavy class now!");
                    } else if(!(gameController.Heavy.contains(player.getName()))) {
                        gameController.Heavy.add(player.getName());
                        classes.giveTools(player);
                        classes.setHeavy(player);
                        player.sendMessage("You are a heavy class now!");
                    }
                }

      

                if (s.equalsIgnoreCase("Light Class")) {
                    if (gameController.Heavy.contains(player.getName())) {
                        gameController.Heavy.remove(player.getName());
                        player.getInventory().clear();
                        for(PotionEffect effect : player.getActivePotionEffects())
                        {
                            player.removePotionEffect(effect.getType());
                        }
                    }

                    if (gameController.Archer.contains(player.getName())) {
                        gameController.Archer.remove(player.getName());
                        player.getInventory().clear();
                        for(PotionEffect effect : player.getActivePotionEffects())
                        {
                            player.removePotionEffect(effect.getType());
                        }
                    }

                    if (gameController.Light.contains(player.getName())) {
                        player.sendMessage("You are a light class now!");
                    } else if(!(gameController.Light.contains(player.getName()))) {
                        gameController.Light.add(player.getName());
                        classes.giveTools(player);
                        classes.setRunner(player);
                        player.sendMessage("You are a light class now!");
                    }
                }
      
                if (s.equalsIgnoreCase("Recon Class")) {
                    
                    if (gameController.Light.contains(player.getName())) {
                        gameController.Light.remove(player.getName());
                        player.getInventory().clear();
                        for(PotionEffect effect : player.getActivePotionEffects())
                        {
                            player.removePotionEffect(effect.getType());
                        }
                    }

                    if (gameController.Heavy.contains(player.getName())) {
                        gameController.Heavy.remove(player.getName());
                        player.getInventory().clear();
                        for(PotionEffect effect : player.getActivePotionEffects())
                        {
                            player.removePotionEffect(effect.getType());
                        }
                    }

                    if (gameController.Archer.contains(player.getName())) {
                        player.sendMessage("You are an archer class now!");
                    } else if (!(gameController.Archer.contains(player.getName()))){
                        gameController.Archer.add(player.getName());
                        classes.giveTools(player);
                        classes.setArcher(player);
                        player.sendMessage("You are an archer class now!");
                    }
                }
      
      
        
              }
          }
      }
}
