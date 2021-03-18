package com.werwolf.game.roles;

import com.werwolf.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Jailor extends Villager {

    private final static Logger LOGGER = LoggerFactory.getLogger(Jailor.class);

    public Jailor(Player player) {
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        setCharacterType(CharacterType.Jailor);
    }

    /**
     *
     * @param target Spieler der nächste RUnde gejailt werden soll
     * @param votings Hashmap, die die Voting der Werewölfe beinhaltet (Falls später auch noch andere darauf zugriff haben sollen)
     */
    @Override
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
        LOGGER.info(getUsername() + " hat " + target.getUsername() + "(" + game.getPlayer(target.getId()).getCharacterType() + ") erflogreich gejailt");
        target.setJailed(true);
    }
}
