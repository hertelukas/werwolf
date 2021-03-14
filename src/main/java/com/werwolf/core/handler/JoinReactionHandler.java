package com.werwolf.core.handler;

import com.werwolf.game.Player;
import com.werwolf.game.PlayerListStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
public class JoinReactionHandler extends ReactionHandler {

    @Override
    public boolean handle(GuildMessageReactionAddEvent event) {

        if (!event.getReactionEmote().getAsReactionCode().equals("✅")) return false;

        TextChannel channel = event.getChannel();

        if (games.containsKey(channel.getIdLong())) {
            if (event.getMessageIdLong() != games.get(channel.getIdLong()).getMainGameMessage()) return false;

            PlayerListStatus result = games.get(channel.getIdLong()).addPlayer(new Player(event.getUser()));
            if(result == PlayerListStatus.successful) {
                updateMainMessage(channel);
            } else if(result == PlayerListStatus.contains)
                channel.sendMessage(event.getUser().getAsMention() + " is already in the game.").queue();
            else if(result == PlayerListStatus.isBanned)
                channel.sendMessage(event.getUser().getAsMention() + " is banned from this game.").queue();
            else
                channel.sendMessage(event.getUser().getAsMention() + " something went wrong.").queue();
        } else {
            channel.sendMessage(event.getUser().getAsMention() + " there is no game in this channel.").queue();
        }
        return true;

    }
}
