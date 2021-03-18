package com.werwolf.game.roles;

public class Hunter_not_implemented extends Villager {

    public Hunter_not_implemented(Player player){
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        characterType = CharacterType.Hunter;
    }
}
