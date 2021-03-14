package com.werwolf.core.handler;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public abstract class ReactionHandler extends Handler{
    public abstract boolean handle(GuildMessageReactionAddEvent event);
}
