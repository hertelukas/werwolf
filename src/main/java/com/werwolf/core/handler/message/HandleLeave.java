package com.werwolf.core.handler.message;

import com.werwolf.game.Game;
import com.werwolf.game.PlayerListStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleLeave extends MessageHandler{

    public HandleLeave(){
        setName("Leave");
        setCommand("leave");
        setDescription("Use this command to leave the game");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if(!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();

        Game currentGame = games.get(channel.getIdLong());
        //If there is no game in this channel, return
        if(currentGame == null) {
            channel.sendMessage(event.getAuthor().getAsMention() + " There is no game in this channel.").queue();
        }
        else{
            PlayerListStatus result = currentGame.removePlayer(event.getAuthor().getIdLong());
            if(result == PlayerListStatus.successful) {
                updateMainMessage(channel);
            } else if(result == PlayerListStatus.containsNot)
                channel.sendMessage(event.getAuthor().getAsMention() + " is not in the game").queue();
            else
                channel.sendMessage(event.getAuthor().getAsMention() + " something went wrong.").queue();
        }

        return true;
    }
}
