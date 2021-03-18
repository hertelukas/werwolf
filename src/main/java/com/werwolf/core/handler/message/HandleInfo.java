package com.werwolf.core.handler.message;

import com.werwolf.core.handler.Handler;
import com.werwolf.game.Game;
import com.werwolf.game.roles.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class HandleInfo extends MessageHandler {

    public HandleInfo() {
        setName("Info");
        setCommand("info");
        setDescription("Get information about the game you are currently in");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if (!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();
        Game game = Handler.games.get(channel.getIdLong());

        if (game == null)
            channel.sendMessage(event.getAuthor().getAsMention() + " there is no game in this Channel.").queue();
        else {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Your game");
            builder.setDescription("Current members: " + game.getPlayers().size()
                    + "\nHost: " + game.getHost().getUsername());
            for (Player player : game.getPlayers()) {
                builder.addField(player.getUsername(), player.getCharacterType().toString(), false);
            }
            channel.sendMessage(builder.build()).queue(m -> m.delete().queueAfter(2, TimeUnit.SECONDS));
        }

        return true;
    }
}
