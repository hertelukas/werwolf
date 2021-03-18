package com.werwolf.core.handler.message;

import com.werwolf.core.handler.Handler;
import com.werwolf.game.Game;
import com.werwolf.game.roles.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class HandleNewGame extends MessageHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(HandleNewGame.class);

    public HandleNewGame() {
        setName("New Game");
        setCommand("newgame");
        setDescription("This command creates a new game");
    }


    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if (!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();
        Game game = games.get(channel.getIdLong());
        Player hostPlayer = new Player(event.getAuthor(), event.getGuild());

        //Try to create a new game with this id
        if (Handler.createGame(channel, hostPlayer, channel.getGuild())) {
            //Wir versuchen den VoiceChannel zu bekommen, falls der Host nicht in einem Voice Channel ist, der Voicechannel nicht existiert usw, dann ignorieren wir den voiceChannel
            try{
                game.setVoiceChannelID(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel()).getIdLong());
            }
            catch (Exception e){
                LOGGER.info("Host is not in a voice channel. " + e.getMessage());
            }
            //Embedded-Message bauen
            StringBuilder playerlistSB = new StringBuilder();
            for (Player player : games.get(channel.getIdLong()).getPlayers()) {
                playerlistSB.append(player.getUsername()).append("\r");
            }
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setThumbnail("https://cdn.pixabay.com/photo/2020/12/28/14/31/wolf-5867343_960_720.png").setTitle("Werewolf: " + channel.getName())
                    .addField("Host:", games.get(channel.getIdLong()).getHost().getUsername(), false)
                    .addField("Player:", playerlistSB.toString(), false).addField("Configurations:", "TUM-MODE: " + games.get(channel.getIdLong()).getTumMode(), false);
            channel.sendMessage(embedBuilder.build()).queue(message -> {
                games.get(channel.getIdLong()).setMainGameMessage(message.getIdLong());
                message.addReaction("✅").queue();
                message.addReaction("❌").queue();
                message.addReaction("▶").queue();
                message.addReaction("tum:821050411620368384").queue();
            });
        } else {
            channel.sendMessage("Can't create new game. A game is already running in this channel.").queue();
        }

        return true;
    }
}
