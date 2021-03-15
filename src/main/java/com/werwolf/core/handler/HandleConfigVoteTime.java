package com.werwolf.core.handler;

import com.werwolf.game.Game;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleConfigVoteTime extends MessageHandler {

    public HandleConfigVoteTime() {
        setName("Set vote time");
        setCommand("setvotetime");
        setDescription("Set the voting time during the day in minutes");
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
                        game.getController().setDayTime(Long.parseLong(args[1]) * 60L * 1000L);
                        // todo change message(s)
                        channel.sendMessage("Vote time set to " + args[1] + " minutes").queue();
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
