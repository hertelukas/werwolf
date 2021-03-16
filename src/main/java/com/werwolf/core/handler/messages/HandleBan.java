package com.werwolf.core.handler.messages;

import com.werwolf.game.Game;
import com.werwolf.game.PlayerListStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleBan extends MessageHandler{

    public HandleBan(){
        setName("Ban");
        setCommand("ban");
        setDescription("Use this command to ban a player");
    }
    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if(!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();

        if(games.containsKey(channel.getIdLong())){
            if(games.get(channel.getIdLong()).getHost().getId() != event.getAuthor().getIdLong()){
                channel.sendMessage(event.getAuthor().getAsMention() + " only the host can ban a player.").queue();
            }
            //Author is host of the game, remove player
            else{
                Game game = games.get(channel.getIdLong());
                String idString = args[1].substring(3, args[1].length() - 1);
                try {
                    long idToBan = Long.parseLong(idString);
                    if(idToBan == event.getAuthor().getIdLong()){
                        channel.sendMessage(event.getAuthor().getAsMention() + " you can't ban yourself.").queue();
                        return true;
                    }
                    PlayerListStatus result = game.banPlayer(idToBan);
                    switch (result){
                        case successful -> channel.sendMessage("Successfully banned " + args[1]).queue();
                        case contains -> channel.sendMessage(args[1] + " is already banned").queue();
                        default -> channel.sendMessage("Something went wrong.").queue();
                    }
                }
                catch (Exception e){
                    channel.sendMessage("Ban failed. Mention the user you want to ban.").queue();
                }
                updateMainMessage(channel);
            }
        }
        else{
            channel.sendMessage(event.getAuthor().getAsMention() + " there is no game in this channel.").queue();
        }
        return true;
    }
}
