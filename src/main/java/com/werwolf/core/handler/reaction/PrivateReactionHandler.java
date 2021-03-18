package com.werwolf.core.handler.reaction;

import com.werwolf.core.handler.Handler;
import com.werwolf.game.Game;
import com.werwolf.game.roles.Player;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public  class PrivateReactionHandler extends Handler {

    public  boolean handle(PrivateMessageReactionAddEvent event) {
        Game game = null;
        for(Game g : games.values()) { // won't work if player is in multiple games
            for (Player p: g.getPlayers())
                game = p.getId() == event.getUserIdLong() ? g : game;
        }
        if (game == null) return false;
        Long votingMessage = game.getCurrentVotingMessage();

        if (votingMessage == null || event.getMessageIdLong() != game.getCurrentVotingMessage()) return false;


        //todo voting itself
        game.getVotingController().vote(event.getReactionEmote().getAsReactionCode(), Objects.requireNonNull(event.getUser()).getIdLong());
       // updateReactions(event.getChannel(), event.getMessageIdLong());

        return false;
    }



}