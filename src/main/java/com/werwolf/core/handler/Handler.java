package com.werwolf.core.handler;


import com.werwolf.game.Game;
import com.werwolf.game.Player;

import java.util.HashMap;

public abstract class Handler {
    static final HashMap<Long, Game> games = new HashMap<>();

    public static boolean createGame(long channelId, Player host){
        //If there is already a game with this channel id, we won't create a new one
        if(games.containsKey(channelId)) return false;

        Game newGame = new Game(channelId, host);
        games.put(channelId, newGame);
        return true;
    }
}
