package com.werwolf.core.handler;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleStart extends MessageHandler{

    public HandleStart() {
        setName("start");
        setCommand("start");
        setDescription("Starts the game");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        //TODO Noch nicht fertig
        //Sendet MessageEmbed, auf das reagiert werden muss, um das Spiel zu starten
        if(!command.equals(getCommand())) return false;

        //Todo Return if event author is not host
        TextChannel channel = event.getChannel();

        if (games.containsKey(channel.getIdLong())) {
            games.get(channel.getIdLong()).start();
            channel.sendMessage("Spiel startet (temporär muss überarbeitet werden)").queue();
        }
        return true;
    }
}