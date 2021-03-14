package com.werwolf.core.handler;

import com.werwolf.game.Game;
import com.werwolf.game.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleInfo extends MessageHandler{

    public HandleInfo(){
        setName("Info");
        setCommand("info");
        setDescription("Get information about the game you are currently in");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if(!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();
        Game game = Handler.games.get(channel.getIdLong());

        if(game == null) channel.sendMessage(event.getAuthor().getAsMention() + " there is no game in this Channel.").queue();
        else{
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Your game");
            builder.setDescription("Current members: " + game.getPlayers().size()
             + "\nHost: " + game.getHost().getUsername());
            for (Player player : game.getPlayers()) {
                builder.addField(player.getUsername(), player.getClass().getName(), false);
            }
            channel.sendMessage(builder.build()).queue();
        }

        return true;
    }
}
