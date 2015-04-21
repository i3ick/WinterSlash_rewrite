package me.i3ick.winterslash;

import java.util.HashMap;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class KillStreakLogic {

    private WinterSlashGameController gameController;
    private WinterSlashMain plugin;
    
    public KillStreakLogic( WinterSlashGameController PassPlug, WinterSlashMain PassPlug2){
        this.gameController = PassPlug;
        this.plugin = PassPlug2;
    }
    

    
    public HashMap<String, KillStreakWolfLogic> wolfObject = new HashMap<String, KillStreakWolfLogic>();
    
    public void killLogic(Player killer, WinterSlashArena arena){
        
        WinterSlashClasses classes = new WinterSlashClasses();
        
        if(gameController.getPlayerData(killer.getName()).getKills() == 3){
            classes.carePackage(killer);
            return;
        }

        if(gameController.getPlayerData(killer.getName()).getKills() == 4) {
            classes.randomPotion(killer);
            return;
        }
        
        if(gameController.getPlayerData(killer.getName()).getKills() == 5){
            
            KillStreakWolfLogic wolf = new KillStreakWolfLogic();
            Random r = new Random();
            String enemy = arena.getRedTeam().get(r.nextInt(arena.getRedTeam().size()));
            Player enemy1 = Bukkit.getServer().getPlayer(enemy);
            wolf.spawnWolf(killer);
            wolf.spawnWolf(killer);
            wolf.setName(killer.getName());
            wolf.setEnemy(enemy1);
            gameController.addToWolfHash(killer.getName(), wolf);
            return;
        }

        
  
    }
    
    public void changeTarget(String ownerName, Player targetName){
        gameController.getWolfData(ownerName).setEnemy(targetName);
        }
}
