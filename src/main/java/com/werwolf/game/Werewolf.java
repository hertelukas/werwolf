package com.werwolf.game;

public class Werewolf extends Player {


    public Werewolf(String username, long id) {
        super(username, id);
    }

    public boolean voteKill(Villager villager) {

        return true;
    }
}
