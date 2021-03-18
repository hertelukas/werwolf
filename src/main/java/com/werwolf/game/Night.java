package com.werwolf.game;

import com.werwolf.game.roles.Player;

import java.util.ArrayList;
import java.util.List;

public class Night {

    private final List<Player> alive;
    private final List<Player> diedtonight;

    private final String story;

    public Night(List<Player> alive, Game game) {
        this.alive = alive;
        story = game.getController().getRandomStory(true);
        diedtonight = new ArrayList<>();
    }

    public List<Player> getAlive() {
        return alive;
    }

    public String getStory() {
        return story;
    }

    public List<Player> getDiedtonight() {
        return diedtonight;
    }
}