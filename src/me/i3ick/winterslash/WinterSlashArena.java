package me.i3ick.winterslash;

import java.util.ArrayList;
import java.util.HashMap;

import me.i3ick.winterslash.WinterSlashTeams.Team;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WinterSlashArena {

    // Arena Name
    private String name;

    
    public static ArrayList<WinterSlashArena> arenaObjects = new ArrayList<WinterSlashArena>();

    public ArrayList<String> red = new ArrayList<String>();
    public ArrayList<String> green = new ArrayList<String>();
    private ArrayList<String> ingamePlayers = new ArrayList<String>();
    private ArrayList<String> frozen = new ArrayList<String>();
    private ArrayList<String> sign = new ArrayList<String>();
    private ArrayList<String> activeGamers = new ArrayList<String>();
    private ArrayList<String> greenteam = new ArrayList<String>();
    private ArrayList<String> redteam = new ArrayList<String>();
    private ArrayList<String> frozengreen = new ArrayList<String>();
    private ArrayList<String> frozenred = new ArrayList<String>();

    // Username suggests using arrays for teams ead of HashMaps!!!!

    private HashMap<String, Team> players = new HashMap<String, Team>();
    private Location redspawn, greenspawn, joinLocation, endLocation;
    private int maxPlayers;
    private int minPlayers;

    
    // Location Constructor

    /**
     * Constructor for gathering information necessary to create a new
     * arena.
     */
    public WinterSlashArena(String arenaName, Location joinLocation,
            Location redLocation, Location greenLocation,
            int minPlayers) {

        // Initializing it all:
        this.name = arenaName;
        this.joinLocation = joinLocation;
        this.redspawn = redLocation;
        this.greenspawn = greenLocation;
        this.minPlayers = minPlayers;

        // add this constructor to arena objects array
        arenaObjects.add(this);

    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        arenaObjects.add(this);
    }
    
    public int getMaxPlayers(){
        return this.maxPlayers;
    }
    
    public void redSpawn(Location redLocation) {
        this.redspawn = redLocation;
        arenaObjects.add(this);
    }
    
    public void greenSpawn(Location greenLocation) {
        this.greenspawn = greenLocation;
        arenaObjects.add(this);
    }
    
    public void lobbySpawn(Location lobbyLocation) {
        this.joinLocation = lobbyLocation;
        arenaObjects.add(this);
    }
    
    
    // Sort teams
    public void addPlayers() {
        for (String p : getPlayers()) {
            if (red.size() > green.size()) {
                green.add(p);
                players.put(p, Team.GREEN);
                return;

            } else {
                red.add(p);
                players.put(p, Team.RED);
                return;
            }
        }
    }
    
    public void setPlayers(Player p){
        ingamePlayers.add(p.getName());
    }
    
    
    public void removePlayers() {
        for (String p : getPlayers()) {
            red.remove(p);
            green.remove(p);
            ingamePlayers.remove(p);
            return;
        }
    }


    



    // Frozen array

    public void setUnfrozen(String player) {
        frozen.add(player);
    }

    public void setFrozen(String player) {
        frozen.remove(player);
    }

    public ArrayList<String> getUnfrozen() {
        return frozen;
    }

    // sign click

    public void setSign(String player) {
        sign.add(player);
    }

    public void clearSign(String player) {
        sign.remove(player);
    }

    public ArrayList<String> getSign() {
        return sign;
    }

    // Game array

    public void setGamers(String player) {
        activeGamers.add(player);
    }

    public void removeGamers(String player) {
        activeGamers.remove(player);
    }

    public ArrayList<String> getGamers() {
        return activeGamers;
    }

    // Returns
    
    public void addRedFrozen(String player) {
        frozenred.add(player);
    }
    
    public void removeRedFrozen(String player) {
        frozenred.remove(player);
    }
    
    public ArrayList<String> getRedFrozen() {
        return frozenred;
    }
    
    public ArrayList<String> getGreenTeam() {
        return green;
    }
    
    public ArrayList<String> getRedTeam() {
        return red;
    }
    
    public ArrayList<String> getGreenFrozen() {
    return frozengreen;
    }
    
    public void addGreenFrozen(String player) {
        frozengreen.add(player);
        }
    
    public void removeGreenFrozen(String player) {
        frozengreen.remove(player);
        }

    public void clearHash(String player) {
        players.remove(player);
    }

    public HashMap<String, Team> getHash() {
        return players;
    }

    public Location getLobbyLocation() {
        return this.joinLocation;
    }

    public Location getRedSpawn() {
        return this.redspawn;
    }

    public Location getGreenSpawn() {
        return this.greenspawn;
    }

    public void setJoinLocation(Location joinLocation) {
        this.joinLocation = joinLocation;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinPlayers() {
        return this.minPlayers;
    }

    public ArrayList<String> getPlayers() {
        return this.ingamePlayers;
    }
    


    // Booleans

    private boolean inGame = false;
    /**
     * Boolean to determine if an arena is ingame or not (auto false)
     */

    private boolean isFrozen = false;

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean isFull() { // Returns weather the arena is full or not
        if (players.size() >= maxPlayers) {
            return true;
        } else {
            return false;
        }
    }

    // To send each player in the arena a message
    public void sendMessage(String message) {
        for (String s : ingamePlayers) {
            Bukkit.getPlayer(s).sendMessage(message);
        }
    }
    
    
    public boolean ifPlayerIsRed(Player p) {
        for (String p1 : players.keySet()) {
        if(players.get(p1) == Team.RED){
            return true;
        }
        }
        return false;
        }


}
