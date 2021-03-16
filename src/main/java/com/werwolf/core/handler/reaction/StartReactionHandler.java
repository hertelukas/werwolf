package com.werwolf.core.handler.reaction;

import com.werwolf.game.Game;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StartReactionHandler extends ReactionHandler{
    private final static Logger LOGGER = LoggerFactory.getLogger(StartReactionHandler.class);

    @Override
    public boolean handle(GuildMessageReactionAddEvent event) {

        TextChannel channel = event.getChannel();

        if (!event.getReactionEmote().getAsReactionCode().equals("▶")) return false;

        if (games.containsKey(channel.getIdLong())) {
            Game game = games.get(channel.getIdLong());
            if (event.getMessageIdLong() != games.get(channel.getIdLong()).getMainGameMessage()) return false;

            updateReactions(channel, event.getMessageIdLong());
            if (game.getHost().getId() == event.getUser().getIdLong()) {
                if (game.isActive()) {
                    channel.sendMessage("Game is already running").queue();
                } else {
                    if (game.start()) {
                        LOGGER.info("Spiel erfolgreich gestartet");
                    } else
                        channel.sendMessage("Something went wrong.").queue();
                }
            } else {
                channel.sendMessage("Only the host can start the game").queue();
            }
        } else {
            channel.sendMessage(event.getUser().getAsMention() + " there is no game in this channel.").queue();
        }
        return true;
    }
}
