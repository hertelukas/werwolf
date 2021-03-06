package com.werwolf.core.handler.reaction;

import com.werwolf.core.handler.Handler;
import com.werwolf.game.Game;
import com.werwolf.game.roles.Player;
import com.werwolf.game.PlayerListStatus;
import com.werwolf.helpers.UserMessageCreator;
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

        if (Handler.games.containsKey(channel.getIdLong())) {
            if (event.getMessageIdLong() != Handler.games.get(channel.getIdLong()).getMainGameMessage()) return false;

            updateReactions(channel, event.getMessageIdLong());
            Game game = Handler.games.get(channel.getIdLong());
            PlayerListStatus result = game.addPlayer(new Player(event.getUser(), event.getGuild()));

            if(result == PlayerListStatus.successful)
                updateMainMessage(channel);
            else if(result == PlayerListStatus.isBanned)
                event.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(UserMessageCreator.getCreator().getMessage(game, "user-banned")).queue());
            else
                LOGGER.info("Something went wrong: " + result.toString());

        } else {
            channel.sendMessage(event.getUser().getAsMention() + " there is no game in this channel.").queue();
        }

        updateReactions(channel, event.getMessageIdLong());
        return true;
    }
}
