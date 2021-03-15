package com.werwolf.core.handler;

import com.werwolf.game.Game;
import com.werwolf.game.GameStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import org.springframework.stereotype.Service;

@Service
public class StartReactionHandler extends ReactionHandler{

    @Override
    public boolean handle(GuildMessageReactionAddEvent event) {

        TextChannel channel = event.getChannel();


        if (!event.getReactionEmote().getAsReactionCode().equals("▶")) return false;

        if (games.containsKey(channel.getIdLong())) {
            Game game = games.get(channel.getIdLong());
            if (event.getMessageIdLong() != games.get(channel.getIdLong()).getMainGameMessage()) return false;

            updateReactions(channel, event.getMessageIdLong());
            if (game.getHost().getId() == event.getUser().getIdLong()) {
                if (game.getStatus() == GameStatus.Running || game.getStatus() == GameStatus.Stopped) {
                    channel.sendMessage("Game is already running").queue();
                } else {
                    if (game.start()) {
                        System.out.println("Spiel erfolgreich gestartet");
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
