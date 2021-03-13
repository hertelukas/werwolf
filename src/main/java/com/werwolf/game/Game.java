package com.werwolf.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private List<Player> players;
    private List<Player> bannedPlayers;
    private Player host;
    private long channelID;


    public Game(long channelID, Player... players) {
        this.channelID = channelID;
        this.players = Arrays.asList(players);
    }

    public boolean start() {
        return true;
    }

    public boolean stop() {
        return true;
    }

    public boolean addPlayer(Player player) {
        this.players.add(player);
        return true;
    }

    public boolean removePlayer(Player player) {
        if (this.players.contains(player)) {
            this.players.remove(player);
            return true;
        }

        return false;
    }

    public boolean banPlayer(Player player) {
        bannedPlayers.add(player);
        return true;
    }
}
