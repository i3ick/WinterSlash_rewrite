package me.i3ick.winterslash;

import java.util.ArrayList;

public class WinterSlashTeams {

    private ArrayList<String> greenteam = new ArrayList<String>();
    private ArrayList<String> redteam = new ArrayList<String>();
    private ArrayList<String> frozenred = new ArrayList<String>();
    private ArrayList<String> frozengreen = new ArrayList<String>();

    public enum Team {
        RED, GREEN;
    }

    // red team array

    public void RedTeamAdd(String player) {
        redteam.add(player);
    }

    public ArrayList<String> GetRedTeam() {
        return redteam;
    }

    // frozen red team array

    public void FrozenRedAdd(String player) {
        frozenred.add(player);
    }

    public void FrozenRedRemove(String player) {
        frozenred.remove(player);
    }

    public ArrayList<String> GetRedFrozenTeam() {
        return frozenred;
    }

    // green team array

    public void GreenTeamAdd(String player) {
        greenteam.add(player);
    }

    public ArrayList<String> GetGreenTeam() {
        return greenteam;
    }

    // frozen green team array

    public void FrozenGreenAdd(String player) {
        frozengreen.add(player);
    }

    public void FrozenGreenRemove(String player) {
        frozengreen.remove(player);
    }

    public ArrayList<String> GetGreenFrozenTeam() {
        return frozengreen;
    }

}
