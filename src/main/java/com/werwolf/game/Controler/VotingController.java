package com.werwolf.game.Controler;

import com.werwolf.game.Player;

import java.util.HashMap;
import java.util.List;

public class VotingController {
    private HashMap<Long, Integer> votings = new HashMap<>();
    private HashMap<String, Long> playerPrefixmap = new HashMap<>();


    public VotingController(){
    }

    public void newVoting() {
        votings = new HashMap<>();
        playerPrefixmap = new HashMap<>();
    }

    public void addPlayer(String playerPrefix, long playerID) {
        playerPrefixmap.put(playerPrefix, playerID);
        votings.put(playerID, 0);
        System.out.println(votings.toString());
    }

    public void vote(String playerPrefix) {
        votings.computeIfPresent(playerPrefixmap.get(playerPrefix), (aLong, integer) -> (integer++));
    }
}

