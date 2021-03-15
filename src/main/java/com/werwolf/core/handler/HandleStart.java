package com.werwolf.core.handler;

import com.werwolf.game.Game;
import com.werwolf.game.GameStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleStart extends MessageHandler {

    public HandleStart() {
        setName("start");
        setCommand("start");
        setDescription("Starts the game");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if (!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();

        if (games.containsKey(channel.getIdLong())) {
            Game game = games.get(channel.getIdLong());

            if (game.getHost().getId() == event.getAuthor().getIdLong()) {
                if (game.getStatus() == GameStatus.Running || game.getStatus() == GameStatus.Stopped) {
                    channel.sendMessage("Game is already running").queue();
                } else {
                    if (game.start())
                        channel.sendMessage("Game started").queue();
                    else
                        channel.sendMessage("Something went wrong.").queue();
                }
            } else {
                channel.sendMessage("Only the host can start the game").queue();
            }
        } else {
            channel.sendMessage(event.getAuthor().getAsMention() + " there is no game in this channel.").queue();
        }
        return true;
    }
}