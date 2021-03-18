package com.werwolf.game.roles;

import com.werwolf.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Bodyguard extends Villager {

    private final static Logger LOGGER = LoggerFactory.getLogger(Bodyguard.class);

    public Bodyguard(Player player) {
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        setCharacterType(CharacterType.Bodyguard);
    }

    /**
     *
     * @param target Spieler der Beschützt werden soll
     * @param votings Hashmap, die die Voting der Werewölfe beinhaltet (Falls später auch noch andere darauf zugriff haben sollen)
     */
    @Override
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
        if (canVote()) {
            LOGGER.info(game.getPlayer(target.getId()).getUsername() + " wird vom Bodyguard " + getUsername() + " beschützt");
            target.setSavedByBodyguyard(true);
        } else {
            setCanVoteTrue(game);
        }
    }
}
