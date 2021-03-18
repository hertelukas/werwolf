package com.werwolf.game;

import com.werwolf.game.roles.Player;

import java.util.List;

public class Day {

    private final List<Player> alive;
    private final String story;

    public Day(List<Player> alive, Game game) {
        this.alive = alive;
        story = game.getController().getRandomStory(false);
    }

    public List<Player> getAlive() {
        return alive;
    }
}
