package com.werwolf.game.specialRoles;

import com.werwolf.game.CharacterType;

public class Witch extends Villager {

    public Witch(Player player) {
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        setCharacterType(CharacterType.Witch);
    }
}
