
package com.werwolf.core.handler.messages;

import com.werwolf.game.Game;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;


@Service
public class HandleConfigWolfTime extends MessageHandler {

    public HandleConfigWolfTime() {
        setName("Set wolf vote time");
        setCommand("setwolftime");
        setDescription("Set the voting time during the night in minutes");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if (!command.equals(getCommand()))
            return false;

        TextChannel channel = event.getChannel();

        if (games.containsKey(channel.getIdLong())) {
            Game game = games.get(channel.getIdLong());

            if (game.getHost().getId() == event.getAuthor().getIdLong()) {
                if (game.isActive()) {
                    channel.sendMessage("Game is already running").queue();
                } else {
                    try {
                        game.getController().setWolfVoteTime(Long.parseLong(args[1]) * 60L * 1000L);
                        // todo change message(s)
                        channel.sendMessage("Furry vote time set to " + args[1] + " minutes").queue();
                    } catch (Exception ex) {
                        channel.sendMessage("Config failed. Include the amount of time you wish to have in minutes.").queue();
                    }
                }
            } else {
                channel.sendMessage("Only the host can change the game configurations").queue();
            }
        } else {
            channel.sendMessage(event.getAuthor().getAsMention() + " there is no game in this channel.").queue();
        }
        return true;
    }
}
