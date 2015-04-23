package me.i3ick.winterslash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.i3ick.winterslash.WinterSlashTeams.Team;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WinterSlashArena {

    // Arena Name
    private String name;

    
    public ArrayList<WinterSlashArena> arenaObjects = new ArrayList<WinterSlashArena>();
    

    Map<Player, Location> PlayerDeathData = new HashMap<Player, Location>();

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
    private ArrayList<String> alive = new ArrayList<String>();
    public ArrayList<String> disableFire = new ArrayList<String>();
    private ArrayList<String> clickedSign = new ArrayList<String>();
    private int roundNumber;
    private int redWins;
    private int greenWins;


    private HashMap<String, Team> players = new HashMap<String, Team>();
    private Location redspawn;
    private Location greenspawn;
    private Location joinLocation;
    private int minPlayers;

    public void reset(){
        this.red.clear();
        this.green.clear();
        this.ingamePlayers.clear();
        this.frozen.clear();
        this.sign.clear();
        this.activeGamers.clear();
        this.greenteam.clear();
        this.redteam.clear();
        this.frozengreen.clear();
        this.frozenred.clear();
        this.alive.clear();
        this.disableFire.clear();
        this.clickedSign.clear();
    }
    
    
    public ArrayList<String> getAlive(){
        return alive;
    }
    
    public ArrayList<String> getClickedSign(){
        return clickedSign;
    }
    
    public void setClicked(Player p){
        clickedSign.add(p.getName());
    }
    
    public void setDeathData(Player player, Location loc){
        PlayerDeathData.put(player, loc);
    }
    
    public Location getDeathData(Player player){
           return PlayerDeathData.get(player);  
    }
    
    // Sort teams
    public void addPlayers() {
        for (String p : getGamers()) {          
            if (red.size() > green.size()) {
                green.add(p);
                players.put(p, Team.GREEN);

            } else {
                red.add(p);
                players.put(p, Team.RED);
            }
        }
    }
    
    public void setPlayers(Player p){
        ingamePlayers.add(p.getName());
    }
    
    
    public void removePlayers() {
        for (String p : getGamers()) {
            red.remove(p);
            green.remove(p);
            ingamePlayers.remove(p);
            return;
        }
    }

    public void minPlayers(int players){this.minPlayers = players;}

    public void nextRound(){this.roundNumber = this.roundNumber + 1; sendBroadcastMessage( this.roundNumber);}
    public int getRound(){return this.roundNumber;}

    public void addRedWin(){this.redWins = this.redWins + 1;}
    public int getRedWins(){return this.redWins;}

    public void addGreenWin(){this.greenWins = this.greenWins + 1;}
    public int getGreenWins(){return this.greenWins;}
    
    public void setRed(Location redSpawn){
        this.redspawn = redSpawn;
    }
    
    public void setGreen(Location greenSpawn){
        this.greenspawn = greenSpawn;
    
    }
    
    public void setLobby(Location lobbyLocation){
        this.joinLocation = lobbyLocation;
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
        return joinLocation;
    }

    public Location getRedSpawn() {
        return redspawn;
    }

    public Location getGreenSpawn() {
        return greenspawn;
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
    
    
    
    /**
     * Boolean to determine if an arena is ingame or not (auto false)
     */
    private boolean inGame = false;
    private boolean isLast = false;
    private boolean isFrozen = false;

    public boolean isInGame() {
        return inGame;
    }

    public boolean isLastRound() {
        return isLast;
    }

    public void setLastRound(boolean isLast){
        this.isLast = isLast;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
        this.roundNumber = 1;
    }

    /**
     * returns whether game is full or not.
     * @return
     */
    public boolean isFull() {
        if (getGamers().size() >= minPlayers) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean ifContains(Player player){
        if(getGamers().contains(player.getName())){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Send a message to all ingame players
     * @param message
     */
    public void sendMessage(String message) {
        for (String s : ingamePlayers) {
            Player player = Bukkit.getServer().getPlayerExact(s);
            if(player != null){
            Bukkit.getPlayer(s).sendMessage(message);
            }
        }
    }

    /**
     * Show a title over everyones screen
     * @param roundNumber
     */
    public void sendBroadcastMessage(int roundNumber) {
        for (String s : ingamePlayers) {
            Player player = Bukkit.getServer().getPlayerExact(s);
            if(player != null){
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName() + " times 5 18 5" );
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title " + player.getName() +
                        " title [{text:Round-,color:red,bold:true},{text:" +
                        roundNumber + ",color:gold,bold:false}]");
            }
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
