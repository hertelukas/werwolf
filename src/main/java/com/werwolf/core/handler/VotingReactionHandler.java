package com.werwolf.core.handler;

import com.werwolf.game.Game;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
public class VotingReactionHandler extends ReactionHandler{
    @Override
    public boolean handle(GuildMessageReactionAddEvent event) {
        Game game = games.get(event.getChannel().getIdLong());
        if (game == null) return false;

        Long votingMessage = game.getCurrentVotingMessage();

        if (votingMessage == null || event.getMessageIdLong() != game.getCurrentVotingMessage()) return false;


        return false;
    }
}
