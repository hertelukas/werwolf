package com.werwolf.core.handler.reaction;

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

        if (game.getController().isMajorVotingFirst() && event.getMessageIdLong() == game.getController().getMajorVoteMessageID()) {
            game.getController().receiveVoteMajor(game.getPlayer(event.getUserIdLong()), event.getReactionEmote().getAsReactionCode());
            updateReactions(event.getChannel(), event.getMessageIdLong());
            return true;
        }

        if (votingMessage == null || event.getMessageIdLong() != game.getCurrentVotingMessage()) return false;

        game.getVotingController().vote(event.getReactionEmote().getAsReactionCode(), event.getUser().getIdLong());
        updateReactions(event.getChannel(), event.getMessageIdLong());

        return true;
    }
}
