package com.werwolf.core.handler;

import com.werwolf.game.Player;
import com.werwolf.game.PlayerListStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JoinReactionHandler extends ReactionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(JoinReactionHandler.class);

    @Override
    public boolean handle(GuildMessageReactionAddEvent event) {

        TextChannel channel = event.getChannel();

        if (!event.getReactionEmote().getAsReactionCode().equals("✅")) return false;

        if (games.containsKey(channel.getIdLong())) {
            if (event.getMessageIdLong() != games.get(channel.getIdLong()).getMainGameMessage()) return false;

            updateReactions(channel, event.getMessageIdLong());

            PlayerListStatus result = games.get(channel.getIdLong()).addPlayer(new Player(event.getUser()));

            //todo maybe send private message with reason
            if(result == PlayerListStatus.successful)
                updateMainMessage(channel);
            else if(result == PlayerListStatus.isBanned)
                channel.sendMessage(event.getUser().getAsMention() + " you are banned").queue();
            else
                LOGGER.info("Something went wrong: " + result.toString());

        } else {
            channel.sendMessage(event.getUser().getAsMention() + " there is no game in this channel.").queue();
        }

        updateReactions(channel, event.getMessageIdLong());
        return true;
    }
}
