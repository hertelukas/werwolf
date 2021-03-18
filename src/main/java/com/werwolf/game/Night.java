package com.werwolf.game;

import com.werwolf.game.roles.Player;

import java.util.List;

public class Night {

    private final List<Player> alive;

    private final String story;

    public Night(List<Player> alive, Game game) {
        this.alive = alive;
        story = game.getController().getRandomStory(true);
    }

    public List<Player> getAlive() {
        return alive;
    }

    public String getStory() {
        return story;
    }
}