package com.werwolf.core.handler.message;

import com.werwolf.core.handler.Handler;
import com.werwolf.core.handler.message.configs.Config;
import com.werwolf.game.Game;
import com.werwolf.game.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Configuration
public class HandleConfig extends MessageHandler {

    private List<Config> configs;

    public HandleConfig(Config... configs) {
        setName("Configurations");
        setCommand("config");
        setDescription("Get information about the possible Configurations");

        this.configs = new ArrayList<>(Arrays.asList(configs));
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if (!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();
        Game game = Handler.games.get(channel.getIdLong());
        System.out.println(Arrays.toString(args));

        if (args.length == 0) {
            EmbedBuilder configBuilder = new EmbedBuilder();
            configBuilder.setTitle("Configurations").setDescription("Use ww!config <configuaration>");
            for (Config config : configs) {
                configBuilder.addField(config.getCommand(), config.getName(), false);
            }

            channel.sendMessage(configBuilder.build()).queue();
            return true;
        }

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