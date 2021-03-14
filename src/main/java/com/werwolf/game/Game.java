package com.werwolf.game;

import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private List<Player> players = new ArrayList<>();
    private final List<Long> bannedPlayerIds = new ArrayList<>();
    private Player host;
    long channelID;
    long voiceChannelID;
    long wolfChannelID;

    public Game(long channelID, Player host, Player... players) {
        this.channelID = channelID;
        this.host = host;
        if(players.length > 0) this.players = Arrays.asList(players);
        this.players.add(host);
    }

    public boolean start() {
        return true; //TODO
    }

    public boolean stop() {
        return true; //TODO
    }

    public PlayerListStatus addPlayer(@NotNull Player player) {
        if(hasPlayer(player.getId())) return PlayerListStatus.contains;
        if(isBanned(player.getId())) return PlayerListStatus.isBanned;
        players.add(player);
        return PlayerListStatus.successful;
    }

    public PlayerListStatus removePlayer(@NotNull Player player) {
        return removePlayer(player.getId());
    }

    public PlayerListStatus removePlayer(long id){
        if(!hasPlayer(id)) return PlayerListStatus.containsNot;
        else{
            for (Player player : players) {
                if(player.getId() == id){
                    players.remove(player);
                    //Todo if size is 0 we want to delete this game
                    if(player.getId() == host.getId() && players.size() > 0){
                        host = players.get(0);
                    }
                    return PlayerListStatus.successful;
                }
            }
        }
        return PlayerListStatus.unsuccessful;
    }

    public PlayerListStatus banPlayer(@NotNull Player player) {
        return banPlayer(player.getId());
    }

    public PlayerListStatus banPlayer(long id){
        if(isBanned(id)) return PlayerListStatus.contains;
        bannedPlayerIds.add(id);
        return PlayerListStatus.successful;
    }

    public PlayerListStatus pardonPlayer(@NotNull Player player) {
        return pardonPlayer(player.getId());
    }

    public PlayerListStatus pardonPlayer(long id){
        if(isBanned(id)){
            bannedPlayerIds.remove(id);
            return PlayerListStatus.successful;
        }
        return PlayerListStatus.containsNot;
    }

    public boolean hasPlayer(long id){
        for (Player player : players) {
            if(player.getId() == id) return true;
        }
        return false;
    }

    public boolean isBanned(long id){
        for (Long bannedPlayer : bannedPlayerIds) {
            if(bannedPlayer == id) return true;
        }
        return false;
    }


    //Getter & Setter
    public List<Player> getPlayers(){
        return players;
    }

    public Player getHost(){
        return host;
    }
}
