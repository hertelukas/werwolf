package com.werwolf.core.handler;

import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ContextException;

public abstract class ReactionHandler extends Handler{
    public abstract boolean handle(GuildMessageReactionAddEvent event);


    void updateReactions(TextChannel channel, long messageID) {
        channel.retrieveMessageById(messageID).queue(message -> {
            try {
                for (MessageReaction reaction : message.getReactions()) {
                    try {
                        reaction.retrieveUsers().queue(users -> {
                            for (User user : users) {
                                if (!user.isBot()) {
                                    System.out.println("test");
                                    try {
                                        message.removeReaction(reaction.getReactionEmote().getAsReactionCode(), user).queue();
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        });
                    } catch (Exception e){}
            }
            }catch (Exception e) {}
        });
    }
}