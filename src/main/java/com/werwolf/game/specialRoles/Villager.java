package com.werwolf.game.specialRoles;

import com.werwolf.game.CharacterType;

public class Villager extends Player {

    public Villager(Player player) {
        super(player.user, player.guild);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        setCharacterType(CharacterType.Villager);
    }
}