package com.werwolf.core.handler;

import com.werwolf.game.Player;
import com.werwolf.game.PlayerListStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleJoin extends MessageHandler {

    public HandleJoin() {
        setName("join");
        setCommand("join");
        setDescription("Use this command to join a game");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if(!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();

        if (games.containsKey(channel.getIdLong())) {
            PlayerListStatus status = games.get(channel.getIdLong()).addPlayer(new Player(event.getAuthor().getName(), event.getAuthor().getIdLong()));
            //TODO Passende Message ausgeben (Im ENUM status ist das wichtigste drinnen)
            //TODO ggf. Überstandswechsel in der Statusmessage updaten
            return true;
        } else {
            //TODO Message die kommt wenn Spieler bereits dem Spiel beigetreten ist
            return true;
        }
    }
}
