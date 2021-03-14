package com.werwolf.core.handler;

import com.werwolf.game.Player;
import com.werwolf.game.PlayerListStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleJoin extends MessageHandler {

    public HandleJoin() {
        setName("Join");
        setCommand("join");
        setDescription("Use this command to join a game");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if(!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();

        if (games.containsKey(channel.getIdLong())) {
            PlayerListStatus result = games.get(channel.getIdLong()).addPlayer(new Player(event.getAuthor()));
            if(result == PlayerListStatus.successful)
                channel.sendMessage(event.getAuthor().getAsMention() + " successfully joined the game!").queue();
            else if(result == PlayerListStatus.contains)
                channel.sendMessage(event.getAuthor().getAsMention() + " is already in the game.").queue();
            else
                channel.sendMessage(event.getAuthor().getAsMention() + " something went wrong.").queue();
            return true;
        } else {
            channel.sendMessage(event.getAuthor().getAsMention() + " There is no game in this channel.").queue();
            return true;
        }
    }
}
