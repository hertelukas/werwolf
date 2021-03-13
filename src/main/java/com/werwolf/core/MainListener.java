package com.werwolf.core;

import com.werwolf.core.handler.MessageHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MainListener extends ListenerAdapter {

    @Value("${prefix}")
    private String prefix;
    private List<MessageHandler> messageHandlers;


    public MainListener(MessageHandler... handlers) {
        this.messageHandlers = Arrays.asList(handlers);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        //Ignore if user is a bot
        if(event.getAuthor().isBot()) return;

        TextChannel channel = event.getChannel();

        String message = event.getMessage().getContentRaw().toLowerCase();

        //Ignore if message does not start with prefix
        if(!message.startsWith(prefix)) return;

        String[] args = message.substring(prefix.length()).trim().split(" ");

        //Only handle help here
        //All other commands are handled by handlers
        if(args[0].equals("help")){
            if(args.length == 2){
                for (MessageHandler messageHandler : messageHandlers) {
                    if(messageHandler.getName().equals(args[1])){
                        channel.sendMessage((messageHandler.help(prefix))).queue();
                        return;
                    }
                }
            }
            else{
                channel.sendMessage(help()).queue();
            }
        }

        if(!commandHandled(event, args[0], args))
            channel.sendMessage("Command not found.").queue();
    }

    private MessageEmbed help() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Main help page");
        builder.setDescription("Type " + prefix + "help <command>");
        return builder.build();
    }

    private boolean commandHandled(GuildMessageReceivedEvent event, String command, String... args){
        boolean found = false;

        for (MessageHandler handler : messageHandlers) {
            found = handler.handle(event, command, args) || found;
        }
        return found;
    }
}
