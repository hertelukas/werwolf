package com.werwolf.core.handler;

import com.werwolf.WerwolfApplication;
import com.werwolf.game.Game;
import com.werwolf.game.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleNewGame extends MessageHandler {

    public HandleNewGame() {
        setName("New Game");
        setCommand("newgame");
        //Todo more precise description on how to use
        setDescription("This command creates a new game.");
    }


    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if (!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();
        Player hostPlayer = new Player(event.getAuthor());

        //Try to create a new game with this id
        if (Handler.createGame(channel.getIdLong(), hostPlayer, channel.getGuild())) {
            //Embeded-MessageBauen
            Game game = games.get(channel.getIdLong());
            StringBuilder playerlistSB = new StringBuilder();
            for (Player player : games.get(channel.getIdLong()).getPlayers()) {
                playerlistSB.append(player.getUsername()).append("\r");
            }
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setThumbnail("https://cdn.pixabay.com/photo/2020/12/28/14/31/wolf-5867343_960_720.png").setTitle("Werewolf: " + channel.getName())
                    .addField("Host:", games.get(channel.getIdLong()).getHost().getUsername(), false)
                    .addField("Player:", playerlistSB.toString(), false).addField("Configurations:", "TUM-MODE: " + "false", false);
            channel.sendMessage(embedBuilder.build()).queue(message -> {
                games.get(channel.getIdLong()).setMainGameMessage(message.getIdLong());
                message.addReaction("✅").queue();
                message.addReaction("❌").queue();
            });
        } else {
            channel.sendMessage("Can't create new game. A game is already running in this channel.").queue();
        }

        return true;
    }
}
