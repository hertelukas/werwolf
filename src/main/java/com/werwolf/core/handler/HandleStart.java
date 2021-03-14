package com.werwolf.core.handler;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleStart extends MessageHandler{

    public HandleStart() {
        setName("start");
        setCommand("start");
        setDescription("Startet deas Spiel");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        //TODO Noch nicht fertig
        if(!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();

        if (games.containsKey(channel.getIdLong())) {
            games.get(channel.getIdLong()).start();
        }
        return true;
    }
}