package com.werwolf.game.roles;

import com.werwolf.game.Game;
import com.werwolf.helpers.UserMessageCreator;

import java.util.HashMap;


public class Nighthunter extends Villager {

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
            } else if (target.isAlive) {
                sendMessage(UserMessageCreator.getCreator().getMessage(game, "hunter-fail"));
            } else {
                sendMessage(UserMessageCreator.getCreator().getMessage(game, "hunter-death"));
            }
        }
    }

    @Override
    public void reset(Game game) {
    }
}
