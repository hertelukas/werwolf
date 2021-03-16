package com.werwolf.core.handler.message;

import com.werwolf.core.MainListener;
import com.werwolf.game.CharacterType;
import com.werwolf.game.Game;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HandleRoles extends MessageHandler {

    @Value("${prefix}")
    private String prefix;


    public HandleRoles() {
        setName("Roles");
        setCommand("roles");
        setDescription("Returns a list of all possible roles");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {


        if (!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();

        CharacterType[] roles = CharacterType.class.getEnumConstants();
        EmbedBuilder roleBuilder = new EmbedBuilder();
        roleBuilder.setTitle("Roles").setDescription("Type " + prefix + "help <role> to get more information");
        StringBuilder rolesListSB = new StringBuilder();
        for (CharacterType role : roles) {
            rolesListSB.append(role.toString()).append("\r");
        }
        roleBuilder.addField("List of all Roles", rolesListSB.toString(), false);
        channel.sendMessage(roleBuilder.build()).queue();
        return true;

    }
}
