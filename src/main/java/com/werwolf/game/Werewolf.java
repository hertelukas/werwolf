package com.werwolf.game;

public class Werewolf extends Player {

    public boolean voteKill(Villager villager) {
        villager.die();
        return true;
    }
}
