package com.werwolf.game.specialRoles;

import com.werwolf.game.CharacterType;
import com.werwolf.game.Game;

import java.util.HashMap;

public class LittleGirl extends Villager {

    public LittleGirl(Player player) {
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        setCharacterType(CharacterType.LittleGirl);
    }

    /**
     * Mädchen kann nicht voten!
     * @param target
     * @param votings
     */
    @Override
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
    }
}