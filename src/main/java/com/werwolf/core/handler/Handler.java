package com.werwolf.core.handler;


import com.werwolf.game.Game;
import com.werwolf.game.roles.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public abstract class Handler {
    private final static Logger LOGGER = LoggerFactory.getLogger(Handler.class);

    public static final HashMap<Long, Game> games = new HashMap<>();
    private static final HashMap<Long, Game> werewolfChannel = new HashMap<>();

    public static boolean createGame(TextChannel channel, Player host, Guild guild) {
        //If there is already a game with this channel id, we won't create a new one
        if (games.containsKey(channel.getIdLong())) return false;

        Game newGame = new Game(channel, host, guild);
        games.put(channel.getIdLong(), newGame);
        return true;
    }


    public void updateMainMessage(TextChannel channel) {
        Game game = games.get(channel.getIdLong());
        if (game.getPlayers().isEmpty()) {
            channel.retrieveMessageById(game.getMainGameMessage()).queue(message -> message.delete().queue());
            game.stop();
            return;
        }
        StringBuilder playerlistSB = new StringBuilder();
        for (Player player : games.get(channel.getIdLong()).getPlayers()) {
            playerlistSB.append(player.getUsername()).append("\r");
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setThumbnail("https://cdn.pixabay.com/photo/2020/12/28/14/31/wolf-5867343_960_720.png").setTitle("Werewolf: " + channel.getName())
                .addField("Host:", games.get(channel.getIdLong()).getHost().getUsername(), false)
                .addField("Player:", playerlistSB.toString(), false).addField("Configurations:", "TUM-MODE: " + game.getTumMode(), false);
        channel.retrieveMessageById(game.getMainGameMessage()).queue(message -> message.editMessage(embedBuilder.build()).queue());
    }

    public static void deleteGame(long id) {
        Game game = games.remove(id);
        if (game == null) LOGGER.info("No game removed.");
    }

    public static void addWerewolfChannel(long id, Game game){
        werewolfChannel.put(id, game);
    }

    public static boolean werewolfChannelHandle(long idLong, String msg) {
        if(!werewolfChannel.containsKey(idLong)) return false;
        werewolfChannel.get(idLong).sendToSpy(msg);
        return true;
    }


}