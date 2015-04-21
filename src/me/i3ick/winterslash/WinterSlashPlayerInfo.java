package me.i3ick.winterslash;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class WinterSlashPlayerInfo {

    private WinterSlashGameController gameController;

    public WinterSlashPlayerInfo(WinterSlashGameController PassPlug){
        this.gameController = PassPlug;
    }

    
    private String name;
    private String team;
    private int killcount;
    
    
    /**
     * Hashmap that keeps all temporary
     * player info.
     */
    public ArrayList<WinterSlashPlayerInfo> stats = new ArrayList<WinterSlashPlayerInfo>(); 
    
    private boolean isAlive = true;



    public boolean isAlive(){
        return isAlive;       
    }
    
    public void setDead(boolean isAlive){
        this.isAlive = false; 
    }
    
    public void addKill(Player p){
        killcount = killcount + 1;
    }
    
    public int getKills(){
        return this.killcount;
    }
    
    public void clearKillstreak(Player p){
        killcount = 0;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
       return this.name;
    }

    public boolean isInGame(){
        for (String arenas : gameController.arenaNameList) {
            WinterSlashArena arena = gameController.getArena(arenas);
            if ((arena.getPlayers().contains(name))) {
                return true;
            }
        }
        return false;
    }
    
}
