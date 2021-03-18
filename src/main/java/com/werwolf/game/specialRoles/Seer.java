package com.werwolf.game.specialRoles;

import com.werwolf.game.CharacterType;
import com.werwolf.game.Game;
import com.werwolf.helpers.UserMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Seer extends Villager {

    private final static Logger LOGGER = LoggerFactory.getLogger(Seer.class);

    public Seer(Player player) {
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        setCharacterType(CharacterType.Seer);
    }

    /**
     *
     * @param target Spieler, von dem der Seer wissen soll, ob es sich um einen Spieler handelt, der zugriff auf den WW-Channel hat
     * @param votings Hashmap, die die Voting der Werewölfe beinhaltet (Falls später auch noch andere darauf zugriff haben sollen)
     */
    @Override
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
        String temp = target.getCharacterType().isCanSeeWWChannel() ? UserMessageCreator.getCreator().getMessage(game, "seer-response-true")
                : UserMessageCreator.getCreator().getMessage(game, "seer-response-false");
        sendMessage(target.getUsername() + temp);
        LOGGER.info(getUsername() + " schaut " + target.getUsername() + "s Rolle an");
    }
}
