package com.werwolf.core.handler;

import com.werwolf.game.Game;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
public class TumReactionHandler extends ReactionHandler {

    @Override
    public boolean handle(GuildMessageReactionAddEvent event) {

        TextChannel channel = event.getChannel();


        if (!event.getReactionEmote().getAsReactionCode().equals("tum:821050411620368384")) return false;

        if (games.containsKey(channel.getIdLong())) {
            Game game = games.get(channel.getIdLong());
            if (event.getMessageIdLong() != games.get(channel.getIdLong()).getMainGameMessage()) return false;

            updateReactions(channel, event.getMessageIdLong());
            if (game.getHost().getId() == event.getUser().getIdLong()) {
                game.setTumMode(!game.getTumMode());
                event.getReaction().removeReaction(event.getUser()).queue();
                updateMainMessage(event.getChannel());
            } else {
                event.getReaction().removeReaction(event.getUser()).queue();
                channel.sendMessage("Only the host can turn the TUM-Mode on or off").queue();
            }
        } else {
            channel.sendMessage(event.getUser().getAsMention() + " there is no game in this channel.").queue();
        }
        return true;
    }

}
