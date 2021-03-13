package com.werwolf.core;

import com.werwolf.core.handler.Handler;
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
    private List<Handler> handlers;

    public MainListener(Handler... handlers) {
        this.handlers = Arrays.asList(handlers);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("TEST").queue();
    }
}
