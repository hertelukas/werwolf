package com.werwolf.core.handler;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleNewGame extends MessageHandler{

    public HandleNewGame(){
        setName("New Game");
        //Todo more precise description on how to use
        setDescription("This command creates a new game.");
    }


    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if(!command.equals("newGame")) return false;

        TextChannel channel = event.getChannel();

        //Todo start a new game
        channel.sendMessage("Starting a new game...").queue();
        return true;
    }
}
