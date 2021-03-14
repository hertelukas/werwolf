package com.werwolf.core.handler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class MessageHandler extends Handler {

    private String name;
    private String description;
    private String command;


    public abstract boolean handle(GuildMessageReceivedEvent event, String command, String[] args);

    public MessageEmbed help(){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Help page for " + name);
        builder.setDescription(description);
        return builder.build();
    }

    public void setCommand(String command){
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
