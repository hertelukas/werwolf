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
        return true; //TODO
    }

    public boolean stop() {
        return true; //TODO
    }

    public PlayerListStatus addPlayer(@NotNull Player player) {
        if(players.contains(player)) return PlayerListStatus.contains;
        players.add(player);
        return PlayerListStatus.successful;
    }

    public PlayerListStatus removePlayer(@NotNull Player player) {
        if (players.contains(player)) {
            players.remove(player);
            return PlayerListStatus.successful;
        }
        return PlayerListStatus.containsNot;
    }

    public PlayerListStatus banPlayer(@NotNull Player player) {
        if(bannedPlayers.contains(player)) return PlayerListStatus.contains;
        bannedPlayers.add(player);
        return PlayerListStatus.successful;
    }

    public PlayerListStatus pardonPlayer(@NotNull Player player){
        if (bannedPlayers.contains(player)) {
            bannedPlayers.remove(player);
            return PlayerListStatus.successful;
        }
        return PlayerListStatus.containsNot;
    }
}
