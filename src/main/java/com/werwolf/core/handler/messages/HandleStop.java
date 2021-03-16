package com.werwolf.core.handler.messages;

import com.werwolf.game.Game;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HandleStop extends MessageHandler{

    private final static Logger LOGGER = LoggerFactory.getLogger(HandleStop.class);

    public HandleStop(){
        setName("Stop");
        setCommand("stop");
        setDescription("Stops a game");
    }


    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if (!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();

        if (games.containsKey(channel.getIdLong())) {
            Game game = games.get(channel.getIdLong());

            if (game.getHost().getId() == event.getAuthor().getIdLong()) {
                if (game.isActive()) {
                    game.stop();
                } else {
                    LOGGER.info("Tried to stop inactive game");
                }
            } else {
                channel.sendMessage(event.getAuthor().getAsMention() + " only the host can stop the game").queue();
            }
        } else {
            channel.sendMessage(event.getAuthor().getAsMention() + " there is no game in this channel.").queue();
        }
        return true;    }
}
