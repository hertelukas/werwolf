package com.werwolf.game.roles;

import com.werwolf.game.Game;
import com.werwolf.helpers.UserMessageCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;


public class Nighthunter extends Villager {

    private final static Logger LOGGER = LoggerFactory.getLogger(Nighthunter.class);

    boolean died = false;

    public Nighthunter(Player player) {
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        characterType = CharacterType.Nighthunter;
    }

    @Override
    public boolean die(Game game) {
        if (super.die(game)) {
            died = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
        if (died) {
            if (target.die(game)) {
                sendMessage(UserMessageCreator.getCreator().getMessage(game, "hunter-death"));
                game.getController().getNightController().getNights().peek().getDiedtonight().add(target);
                LOGGER.info("Der Jäger bringt " + target.getUsername() + " um");
            } else if (target.isAlive) {
                sendMessage(UserMessageCreator.getCreator().getMessage(game, "hunter-fail"));
                LOGGER.info("Der Jäger schafft es nicht " + target.getUsername() + " umzubringen");
            } else {
                sendMessage(UserMessageCreator.getCreator().getMessage(game, "hunter-death"));
                LOGGER.info("Der Jäger versucht " + target.getUsername() + " umzubringen, das Ziel ist aber schon Tdd");
            }
        }
    }

    @Override
    public void reset(Game game) {
    }
}
