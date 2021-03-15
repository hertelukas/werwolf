package com.werwolf.core.handler;

import com.werwolf.game.Game;
import com.werwolf.game.PlayerListStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
public class LeaveReactionHander extends ReactionHandler {
    @Override
    public boolean handle(GuildMessageReactionAddEvent event) {

        TextChannel channel = event.getChannel();

        if (!event.getReactionEmote().getAsReactionCode().equals("❌")) return false;

        Game currentGame = games.get(channel.getIdLong());
        //If there is no game in this channel, return
        if (currentGame == null) {
            channel.sendMessage(event.getUser().getAsMention() + " There is no game in this channel.").queue();
        } else {
            if (event.getMessageIdLong() != games.get(channel.getIdLong()).getMainGameMessage()) return false;
            updateReactions(channel, event.getMessageIdLong());
            PlayerListStatus result = currentGame.removePlayer(event.getUser().getIdLong());
            if (result == PlayerListStatus.successful) {
                updateMainMessage(channel);
            } else
                System.out.println("Failed to leave game: " + result.toString());
        }

        return true;
    }
}
