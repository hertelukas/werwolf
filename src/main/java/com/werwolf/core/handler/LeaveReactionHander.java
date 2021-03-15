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
        if(currentGame == null) {
            channel.sendMessage(event.getUser().getAsMention() + " There is no game in this channel.").queue();
        }
        else{
            if (event.getMessageIdLong() != games.get(channel.getIdLong()).getMainGameMessage()) return false;
            //Todo handle what happens if player is host
            updateReactions(channel, event.getMessageIdLong());
            PlayerListStatus result = currentGame.removePlayer(event.getUser().getIdLong());
            if(result == PlayerListStatus.successful) {
                // "oldest" player becomes host if host leaves
                if(!currentGame.getPlayers().contains(currentGame.getHost()) && !currentGame.getPlayers().isEmpty())
                    currentGame.setHost(currentGame.getPlayers().get(0));

                updateMainMessage(channel);
            } else if(result == PlayerListStatus.containsNot)
                channel.sendMessage(event.getUser().getAsMention() + " is not in the game").queue();
            else
                channel.sendMessage(event.getUser().getAsMention() + " something went wrong.").queue();
        }

        return true;
    }
}
