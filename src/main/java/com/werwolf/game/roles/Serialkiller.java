package com.werwolf.game.roles;

import com.werwolf.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Serialkiller extends Player{

    private final static Logger LOGGER = LoggerFactory.getLogger(Serialkiller.class);

    public Serialkiller(Player player) {
        super(player.user, player.guild);
        this.characterType = CharacterType.SerialKiller;
    }

    /**
     * SerialKiller tötet den ausgewählten Spieler
     * @param target Spieler der durch den serialkiller getötet wird
     * @param votings Hashmap, die die Voting der Werewölfe beinhaltet (Falls später auch noch andere darauf zugriff haben sollen)
     * @param game
     */
    @Override
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
        if (!canVote()) return;

        if (target.die(game)) {
            game.getController().getNightController().getNights().peek().getDiedtonight().add(target);
            LOGGER.info(getUsername() + "(SerialKiller) hat " + target.getUsername() + " umgebracht");
        }

    }

    /**
     * SerialKiller dont need to reset anything
     * @param game
     */
    @Override
    public void reset(Game game) {
        super.reset(game);
    }
}
