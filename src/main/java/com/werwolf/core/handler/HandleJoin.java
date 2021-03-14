package com.werwolf.core.handler;

import com.werwolf.game.Game;
import com.werwolf.game.Player;
import com.werwolf.game.PlayerListStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleJoin extends MessageHandler {

    public HandleJoin() {
        setName("Join");
        setCommand("join");
        setDescription("Use this command to join a game");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if(!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();

        if (games.containsKey(channel.getIdLong())) {
            PlayerListStatus result = games.get(channel.getIdLong()).addPlayer(new Player(event.getAuthor()));
            if(result == PlayerListStatus.successful) {

                Game game = games.get(channel.getIdLong());
                StringBuilder playerlistSB = new StringBuilder();
                for (Player player : games.get(channel.getIdLong()).getPlayers()) {
                    playerlistSB.append(player.getUsername() + "\r");
                }
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setThumbnail("https://cdn.pixabay.com/photo/2020/12/28/14/31/wolf-5867343_960_720.png").setTitle("Werewolf: " + channel.getName())
                        .addField("Host:", games.get(channel.getIdLong()).getHost().getUsername(), false)
                        .addField("Player:", playerlistSB.toString(), false).addField("Configurations:", "TUM-MODE: " + "false", false);
                channel.sendMessage(embedBuilder.build()).queue();

            } else if(result == PlayerListStatus.contains)
                channel.sendMessage(event.getAuthor().getAsMention() + " is already in the game.").queue();
            else
                channel.sendMessage(event.getAuthor().getAsMention() + " something went wrong.").queue();
            return true;
        } else {
            channel.sendMessage(event.getAuthor().getAsMention() + " there is no game in this channel.").queue();
            return true;
        }
    }
}
