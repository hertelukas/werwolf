package com.werwolf.core.handler.reaction;

import com.werwolf.core.handler.Handler;
import com.werwolf.game.Game;
import com.werwolf.game.roles.CharacterType;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public  class PrivateReactionHandler extends Handler {

    private final static Logger LOGGER = LoggerFactory.getLogger(PrivateReactionHandler.class);

    public  boolean handle(PrivateMessageReactionAddEvent event) {



        Game game = null;
        for(Game g : games.values()) { // won't work if player is in multiple games
            if (g.getController().getMajorVoteMessageID() == event.getMessageIdLong()) {
                game = g;
                break;
            }
        }

        if (game == null) {
            return false;
        }
        if (game.getController().isMayorNormalVoting()) {

            LOGGER.info(event.getUser().getName() + " hat erfolgreich als ehemaliger Bürgermeister seine Stimmme abgegeben für: " + event.getReactionEmote().getAsReactionCode());
            game.getController().receiveVoteMayor(game.getPlayer(event.getUserIdLong()), event.getReactionEmote().getAsReactionCode());
            return true;
        }
        return false;
    }
}