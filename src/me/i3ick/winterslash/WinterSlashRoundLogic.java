package me.i3ick.winterslash;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Karlo on 4/23/2015.
 */
public class WinterSlashRoundLogic {

    private WinterSlashGameController gameController;
    private WinterSlashMain plugin;
    public WinterSlashRoundLogic(WinterSlashMain PassPlug, WinterSlashGameController PassPlug2){
        this.plugin = PassPlug;
        this.gameController = PassPlug2;
    }


    public void calculate(WinterSlashArena arena) {





        if(!(arena.getRound() == gameController.lastRound)){

            if (arena.getGreenFrozen().size() == arena.getGreenTeam().size()){
                arena.sendMessage(ChatColor.GREEN + "RED team won the round!");
                arena.addRedWin();
            }

            if (arena.getRedFrozen().size() == arena.getRedTeam().size()){
                arena.sendMessage(ChatColor.GREEN + "GREEN team won the round!");
                arena.addGreenWin();
            }

            // INITIALIZE NEXT ROUND
            arena.nextRound();
            WinterSlashClasses classes = new WinterSlashClasses();
            for(String p : arena.getPlayers()) {
                Player pl = Bukkit.getServer().getPlayer(p);
                if (arena.getRedTeam().contains(pl.getName())) {
                    pl.teleport(arena.getRedSpawn());
                    classes.redArmor(pl);
                    if (!(gameController.Light.contains(pl)) || (!gameController.Heavy.contains(pl)) || (!gameController.Archer.contains(pl))) {
                        arena.setUnfrozen(pl.getName());
                        arena.removeRedFrozen(pl.getName());
                        classes.setDefault(pl);
                    }
                } else {
                    pl.teleport(arena.getGreenSpawn());
                    classes.greenArmor(pl);
                    if ((!gameController.Light.contains(pl)) || (!gameController.Heavy.contains(pl)) || (!gameController.Archer.contains(pl))) {
                        arena.setUnfrozen(pl.getName());
                        arena.removeGreenFrozen(pl.getName());
                        classes.setDefault(pl);
                    }
                }
            }


            return;
        }


        // End game if red wins
        if(arena.getRedWins() > arena.getGreenWins()){
            arena.sendMessage(ChatColor.GREEN + "The RED team has won the game!");
            for(String b: arena.getGreenTeam()){
                Player pl = Bukkit.getPlayer(b);
                if(!(arena.getAlive().contains(pl.getName()))){
                    gameController.addRestore(pl.getName());
                }

            }
            //  give awards and end
            for(String a: arena.getRedTeam()){
                Player pl = Bukkit.getPlayer(a);
                awardMoney(arena, pl, a);
            }
            return;
        }



        // End game if green wins
        if(arena.getRedWins() < arena.getGreenWins()){
           arena.sendMessage(ChatColor.GREEN + "The GREEN team has won the game!");
            for(String b: arena.getRedTeam()){
                Player pl = Bukkit.getPlayer(b);
                if(!(arena.getAlive().contains(pl.getName()))){
                    gameController.addRestore(pl.getName());
                }

            }

            //  give awards and end
            for(String a: arena.getGreenTeam()){
                Player pl = Bukkit.getPlayer(a);
                awardMoney(arena, pl, a);
            }
            return;
        }


        // IF A TIE, PLAY BONUS ROUND
        if(arena.getRedWins() == arena.getGreenWins()){
            arena.sendMessage(ChatColor.RED + "It's a tie! Playing bonus round!");

            arena.nextRound();
            WinterSlashClasses classes = new WinterSlashClasses();
            for(String p : arena.getPlayers()) {
                Player pl = Bukkit.getServer().getPlayer(p);
                if (arena.getRedTeam().contains(pl.getName())) {
                    pl.teleport(arena.getRedSpawn());
                    classes.redArmor(pl);
                    if (!(gameController.Light.contains(pl)) || (!gameController.Heavy.contains(pl)) || (!gameController.Archer.contains(pl))) {
                        arena.setUnfrozen(pl.getName());
                        arena.removeRedFrozen(pl.getName());
                        classes.setDefault(pl);
                    }
                } else {
                    pl.teleport(arena.getGreenSpawn());
                    classes.greenArmor(pl);
                    if ((!gameController.Light.contains(pl)) || (!gameController.Heavy.contains(pl)) || (!gameController.Archer.contains(pl))) {
                        arena.setUnfrozen(pl.getName());
                        arena.removeGreenFrozen(pl.getName());
                        classes.setDefault(pl);
                    }
                }
            }

        }
    }


    public void awardMoney(WinterSlashArena arena, Player pl, String a){
        WinterSlashEvents events = new WinterSlashEvents(plugin, gameController);
        if(arena.getAlive().contains(pl.getName())){
        gameController.awardMoney(pl);
        }else{gameController.Winner.add(a);}events.theEnd(pl, arena.getName());}
}
