package com.werwolf.game;

import com.werwolf.game.controller.GameController;

import java.util.List;

public class Night {

    private final List<Player> alive;

    private final String story;

    private final boolean tumMode;

    public Night(List<Player> alive,boolean tumMode) {
        this.alive = alive;
        this.tumMode = tumMode;
        story = GameController.getRandomStory(tumMode,true);
    }

    public List<Player> getAlive() {
        return alive;
    }

    public String getStory() {
        return story;
    }
}