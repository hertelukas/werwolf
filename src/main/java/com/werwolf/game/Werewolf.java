package com.werwolf.game;

public class Werewolf extends Player {

    public Werewolf(Player player){
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        characterType = CharacterType.Werewolf;
    }

    public boolean voteKill(Villager villager) {
        return true;
    }
}