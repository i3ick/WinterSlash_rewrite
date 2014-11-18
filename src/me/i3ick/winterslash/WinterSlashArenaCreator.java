package me.i3ick.winterslash;

import java.util.ArrayList;

import org.bukkit.Location;

public class WinterSlashArenaCreator {
    
    public static ArrayList<WinterSlashArenaCreator> arenaObjects = new ArrayList<WinterSlashArenaCreator>();
    
    private  String name;
    private  Location redspawn;
    private  Location greenspawn;
    private  Location joinLocation;
    private int minPlayers;
    
    // Location Constructor

    /**
     * Constructor for gathering information necessary to create a new
     * arena.
     */

    
    public WinterSlashArenaCreator(String arenaName, Location joinLocation,
            Location redLocation, Location greenLocation,
            int minPlayers) {
        
        this.name = arenaName;
        this.joinLocation = joinLocation;
        this.redspawn = redLocation;
        this.greenspawn = greenLocation;
        this.minPlayers = minPlayers;
        arenaObjects.add(this);

    }

    
    public WinterSlashArenaCreator(WinterSlashMain winterSlashMain) {
    }


    public void name(String arenaName) {
        this.name = arenaName;
    }
    
    public void minPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }
    
    public void redSpawn(Location redLocation) {
        this.redspawn = redLocation;
    }
    
    public void greenSpawn(Location greenLocation) {
        this.greenspawn = greenLocation;
    }
    
    public void lobbySpawn(Location lobbyLocation) {
        this.joinLocation = lobbyLocation;
    }
    
    public Location getRedSpawn(){
        return redspawn;
        
    }
    
    public Location getGreenSpawn(){
        return greenspawn;
        
    }
    
    public Location getLobbySpawn(){
        return joinLocation;
        
    }


    public String getName() {
        return this.name;
    }

}
