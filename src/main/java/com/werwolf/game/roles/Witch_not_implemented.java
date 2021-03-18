package com.werwolf.game.roles;

public class Witch_not_implemented extends Villager {

    public Witch_not_implemented(Player player) {
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        setCharacterType(CharacterType.Witch);
    }
}
