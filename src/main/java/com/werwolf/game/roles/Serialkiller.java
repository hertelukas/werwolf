package com.werwolf.game.roles;

import com.werwolf.game.Game;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

public class Serialkiller extends Player{

    public Serialkiller(Player player) {
        super(player.user, player.guild);
        this.characterType = CharacterType.SerialKiller;
    }

    @Override
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
        target.die(game);
    }
}
