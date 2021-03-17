package com.werwolf.core.handler.message;

import com.werwolf.core.handler.Handler;
import com.werwolf.core.handler.message.configs.Config;
import com.werwolf.game.Game;
import com.werwolf.game.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${prefix}")
    private String prefix;


    public HandleConfig(Config... configs) {
        setName("Configurations");
        setCommand("config");
        setDescription("Get information about the possible configurations");

        this.configs = new ArrayList<>(Arrays.asList(configs));
    }

    @Override
    public MessageEmbed help() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Help page for Configurations");
        builder.setDescription("All available configurations. \nType " + prefix + "config <config> <value> to configure a game.");

        for (Config config : configs) {
            builder.addField(config.getCommand(), config.getDescription(), false);
        }
        return builder.build();
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if (!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();
        Game game = Handler.games.get(channel.getIdLong());


        if (game == null)
            channel.sendMessage(event.getAuthor().getAsMention() + " there is no game in this Channel.").queue();
        else {
            if (args.length == 0) {
                EmbedBuilder configBuilder = new EmbedBuilder();
                configBuilder.setTitle("Configurations").setDescription("Use ww!config <config> <value>");
                for (Config config : configs) {
                    configBuilder.addField(config.getName(), config.getCommand() + ": " + config.getConfigResult(game), false);
                }

                channel.sendMessage(configBuilder.build()).queue();
                return true;
            } else {
                for (Config config : configs) {
                    if (args.length == 1) {
                        config.updateConfig(game, args[0], null);
                    } else {
                        config.updateConfig(game, args[0], args[1]);
                    }
                }

            }
        }

        return true;
    }
}