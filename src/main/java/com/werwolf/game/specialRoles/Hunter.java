package com.werwolf.game.specialRoles;

import com.werwolf.game.CharacterType;

public class Hunter extends Villager {

    public Hunter(Player player){
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        characterType = CharacterType.Hunter;
    }
}
