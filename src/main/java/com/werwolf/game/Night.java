package com.werwolf.game;

import java.util.List;

public class Night {

    private List<Player> alive;
    private String story = "die Werewölfe begeben sich auf lauer und suchen sich ein Opfer aus, aber auch der Seher ist unterwegs!";

    public Night(List<Player> alive) {
        this.alive = alive;
    }

    public List<Player> getAlive() {
        return alive;
    }

    public String getStory() {
        return story;
    }
}
