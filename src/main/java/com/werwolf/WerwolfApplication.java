package com.werwolf;

import com.werwolf.game.Game;
import com.werwolf.game.Player;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
public class WerwolfApplication {

    private static final HashMap<Long, Game> games = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(WerwolfApplication.class, args);
    }

    public static boolean createGame(long channelId, Player host){
        //If there is already a game with this channel id, we won't create a new one
        if(games.containsKey(channelId)) return false;

        Game newGame = new Game(channelId, host);
        games.put(channelId, newGame);
        return true;
    }

}
