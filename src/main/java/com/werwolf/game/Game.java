package com.werwolf.game;

import org.jetbrains.annotations.NotNull;
;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private List<Player> players;
    private List<Player> bannedPlayers;
    private Player host;
    private long channelID;
    private GameController gameController;


    public Game(long channelID, Player... players) {
        this.channelID = channelID;
        this.players = new ArrayList<>(Arrays.asList(players));
        this.gameController = new GameController(this);
    }

    public boolean start() {
        if (gameController.isActive()) return false;
        gameController.setActive(true);
        return true; //TODO
    }

    public boolean stop() {
        return true; //TODO
    }

    public PlayerListStatus addPlayer(@NotNull Player player) {
        if (gameController.isActive()) return PlayerListStatus.gameStartet;
        if (players.contains(player)) return PlayerListStatus.contains;
        players.add(player);
        return PlayerListStatus.successful;
    }

    public PlayerListStatus removePlayer(@NotNull Player player) {
        if (gameController.isActive()) return PlayerListStatus.gameStartet;
        if (players.contains(player)) {
            players.remove(player);
            return PlayerListStatus.successful;
        }
        return PlayerListStatus.containsNot;
    }

    public PlayerListStatus banPlayer(@NotNull Player player) {
        if (gameController.isActive()) return PlayerListStatus.gameStartet;
        if (bannedPlayers.contains(player)) return PlayerListStatus.contains;
        bannedPlayers.add(player);
        return PlayerListStatus.successful;
    }

    public PlayerListStatus pardonPlayer(@NotNull Player player) {
        if (gameController.isActive()) return PlayerListStatus.gameStartet;
        if (bannedPlayers.contains(player)) {
            bannedPlayers.remove(player);
            return PlayerListStatus.successful;
        }
        return PlayerListStatus.containsNot;
    }
}
