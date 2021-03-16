package com.werwolf.core.handler.reaction;

import com.werwolf.core.handler.Handler;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public abstract class ReactionHandler extends Handler {
    public abstract boolean handle(GuildMessageReactionAddEvent event);


    public void updateReactions(TextChannel channel, long messageID) {
        if (games.get(channel.getIdLong()) == null) return;
        if (games.get(channel.getIdLong()).getPlayers().size() < 1) return;

        channel.retrieveMessageById(messageID).queue(message -> {
            for (MessageReaction reaction : message.getReactions()) {
                reaction.retrieveUsers().queue(users -> {
                    for (User user : users) {
                        if (!user.isBot()) {
                            message.removeReaction(reaction.getReactionEmote().getAsReactionCode(), user).queue();
                        }
                    }
                });
            }
        });
    }
}