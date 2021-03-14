package com.werwolf.core.handler;

import com.werwolf.WerwolfApplication;
import com.werwolf.game.Player;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleNewGame extends MessageHandler{

    public HandleNewGame(){
        setName("New Game");
        setCommand("newgame");
        //Todo more precise description on how to use
        setDescription("This command creates a new game.");
    }


    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if(!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();
        Player hostPlayer = new Player(event.getAuthor());

        //Try to create a new game with this id
        if(Handler.createGame(channel.getIdLong(),hostPlayer)){
            channel.sendMessage(event.getAuthor().getAsMention() + " created a new game!").queue();
        }else{
            channel.sendMessage("Can't create new game. A game is already running in this channel.").queue();
        }

        return true;
    }
}
