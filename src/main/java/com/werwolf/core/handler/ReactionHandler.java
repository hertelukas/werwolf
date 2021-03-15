package com.werwolf.core.handler;

import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ContextException;

public abstract class ReactionHandler extends Handler{
    public abstract boolean handle(GuildMessageReactionAddEvent event);


    void updateReactions(TextChannel channel, long messageID) {
        if(games.get(channel.getIdLong()).getPlayers().size() <= 1) return;

        channel.retrieveMessageById(messageID).queue(message -> {
            for (MessageReaction reaction : message.getReactions()) {
                reaction.retrieveUsers().queue(users -> {
                    for (User user : users) {
                        if (!user.isBot()) {
                            System.out.println("test");
                            message.removeReaction(reaction.getReactionEmote().getAsReactionCode(), user).queue();
                        }
                    }
                });
            }
        });
    }
}