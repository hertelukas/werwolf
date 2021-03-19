package com.werwolf.core.handler.reaction;

import com.werwolf.core.handler.Handler;
import com.werwolf.game.Game;
import com.werwolf.game.roles.CharacterType;
import com.werwolf.game.roles.Player;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public  class PrivateReactionHandler extends Handler {

    public  boolean handle(PrivateMessageReactionAddEvent event) {
        Game game = null;
        System.out.println("bruh");
        for(Game g : games.values()) { // won't work if player is in multiple games
            for (Player p: g.getPlayers()) {
                game = (p.getUser().equals(event.getUser()) && p.getCharacterType() == CharacterType.Hunter) ? g : game;
            }
        }
        if (game != null) System.out.println("wow");
        if (game == null) {
            System.out.println("huh");
            return false;
        }


        Objects.requireNonNull(event.getUser()).openPrivateChannel().queue(message -> message.sendMessage("successful react").queue());

        // Long votingMessage = game.getCurrentVotingMessage();

        // if (votingMessage == null || event.getMessageIdLong() != game.getCurrentVotingMessage()) return false;


        //todo voting itself (maybe for witch as well)

        Game finalGame = game;
        game.getPlayers().stream()
                .filter(p -> p.getCharacterType() == CharacterType.Hunter && p.getId() == event.getUserIdLong())
                .findFirst()
                .ifPresent(hunter -> hunter.vote(event.getReactionEmote().getAsReactionCode(), event.getMessageIdLong(), finalGame));


        // game.getVotingController().vote(event.getReactionEmote().getAsReactionCode(), Objects.requireNonNull(event.getUser()).getIdLong());
       // updateReactions(event.getChannel(), event.getMessageIdLong());

        return false;
    }



}