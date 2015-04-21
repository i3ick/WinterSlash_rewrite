package me.i3ick.winterslash;



import org.bukkit.DyeColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

public class KillStreakWolfLogic {


    private String name;
    private String owner;
    private Player enemy;
    
    public void spawnWolf(Player owner){
        Wolf wolf = (Wolf) owner.getWorld().spawnEntity(owner.getLocation(), EntityType.WOLF);
        
        wolf.setAdult();
        wolf.setHealth(8.0);
        wolf.setOwner(owner);
        wolf.setCustomName("FIREWOLF");
        wolf.setCustomNameVisible(true);
        wolf.setCollarColor(DyeColor.BLUE);
        wolf.setTarget(getEnemy());
        wolf.setAngry(true);
    }
    
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getOwner(){
        return owner;
    }
    
    public void setOwner(String name){
        this.owner = owner;
    }
    
    public Player getEnemy(){
        return enemy;
    }
    
    public void setEnemy(Player name){
        this.enemy = enemy;
    }
    
    
}
