package com.werwolf.core.handler;


import com.werwolf.game.Game;

import java.util.HashMap;

public abstract class Handler {
    static HashMap<Long, Game> games = new HashMap<>();
}
