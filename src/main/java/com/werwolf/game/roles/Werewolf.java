package com.werwolf.game.roles;

import com.werwolf.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Werewolf extends Player {


    private final static Logger LOGGER = LoggerFactory.getLogger(Werewolf.class);

    public Werewolf(Player player){
        super(player.user, player.guild);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        characterType = CharacterType.Werewolf;
    }

    /**
     * Werewolf stimmt ab
     * @param target Spieler der gevotet wurde
     * @param votings Liste der Stimmen der Werewölfe
     */
    @Override
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
        if (canVote()) {
            if (target.getCharacterType() != CharacterType.Werewolf) {
                LOGGER.info(game.getPlayer(target.getId()).getUsername() + " wurde vom Werewolf " + getUsername() + " ausgewählt");
                votings.computeIfPresent(target.getId(), (aLong, integer) -> (integer = integer + 1));
            }

        } else {
            setCanVoteTrue(game);
        }
    }
}