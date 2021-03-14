package com.werwolf.core.handler;

import com.werwolf.game.Game;
import com.werwolf.game.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
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

        //Todo start a new game
        //TODO fancy spielübersicht (alle Spieler, Einstellungen etc.)
        channel.sendMessage("Starting a new game...").queue();
        createNewGame(channel.getIdLong(), new Player(event.getAuthor().getName(), channel.getIdLong()));
        return true;
    }

    private void createNewGame(long channelID, Player player) {
        games.put(channelID, new Game(channelID, player));
    }
}
