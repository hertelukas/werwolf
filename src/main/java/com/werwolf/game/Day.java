package com.werwolf.game;

import java.util.List;

public class Day {

    private List<Player> alive;
    private String story;

    public Day(List<Player> alive) {
        this.alive = alive;
    }

    public List<Player> getAlive() {
        return alive;
    }
}
