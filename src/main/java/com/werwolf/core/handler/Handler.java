package com.werwolf.core.handler;


import com.werwolf.game.Game;
import com.werwolf.game.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;

public abstract class Handler {
    static final HashMap<Long, Game> games = new HashMap<>();

    public static boolean createGame(TextChannel channel, Player host, Guild guild) {
        //If there is already a game with this channel id, we won't create a new one
        if (games.containsKey(channel.getIdLong())) return false;

        Game newGame = new Game(channel, host, guild);
        games.put(channel.getIdLong(), newGame);
        return true;
    }

    void updateMainMessage(TextChannel channel) {
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
                .addField("Player:", playerlistSB.toString(), false).addField("Configurations:", "TUM-MODE: " + "false", false);
        channel.retrieveMessageById(game.getMainGameMessage()).queue(message -> message.editMessage(embedBuilder.build()).queue());
    }

    public static void deleteGame(long id){
        Game game = games.remove(id);
        if(game == null) System.out.println("No game removed.");
    }
}
