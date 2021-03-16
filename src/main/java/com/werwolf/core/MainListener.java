package com.werwolf.core;

import com.werwolf.core.handler.message.MessageHandler;
import com.werwolf.core.handler.reaction.ReactionHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@Service
public class MainListener extends ListenerAdapter {

    @Value("${prefix}")
    private String prefix;
    private final List<MessageHandler> messageHandlers;
    private final List<ReactionHandler> reactionHandlers;


    public MainListener(List<ReactionHandler> reactionHandlers, MessageHandler... handlers) {
        this.messageHandlers = Arrays.asList(handlers);
        this.reactionHandlers = reactionHandlers;
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
                    if(messageHandler.getCommand().equals(args[1])){
                        channel.sendMessage(messageHandler.help()).queue();
                        return;
                    }
                }
            }
            else{
                channel.sendMessage(help()).queue();
                return;
            }
        }

        String[] arguments = new String[args.length-1];
        for (int i=1; i < args.length; i++) {
            arguments[i-1] = args[i];
        }

        if(!commandHandled(event, args[0], arguments))
            channel.sendMessage("Command not found.").queue();
    }

    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
        boolean found = false;
        if (event.getUser().isBot()) return;

        for (ReactionHandler handler : reactionHandlers) {
            found = handler.handle(event) || found;
        }
    }

    private MessageEmbed help() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Main help page");
        builder.setDescription("All available commands. \nType " + prefix + "help <command> to get detailed information.");
        for (MessageHandler messageHandler : messageHandlers) {
            builder.addField(messageHandler.getCommand(), messageHandler.getDescription(), false);
        }
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
