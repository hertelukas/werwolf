package com.werwolf.game;

import com.werwolf.game.Controler.GameController;

import java.util.List;

public class Day {

    private final List<Player> alive;
    private final String story;

    private final boolean tumMode;

    public Day(List<Player> alive, boolean tumMode) {
        this.alive = alive;
        this.tumMode = tumMode;
        story = GameController.getRandomStory(tumMode,false);
    }

    public List<Player> getAlive() {
        return alive;
    }
}
