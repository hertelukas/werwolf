package com.werwolf.core.handler;

import com.werwolf.game.Game;
import com.werwolf.game.PlayerListStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleUnban extends MessageHandler{

    public HandleUnban(){
        setName("Unban");
        setCommand("unban");
        setDescription("Use this command to unban a player");
    }

    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if(!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();

        if(games.containsKey(channel.getIdLong())){
            if(games.get(channel.getIdLong()).getHost().getId() != event.getAuthor().getIdLong()){
                channel.sendMessage(event.getAuthor().getAsMention() + " only the host can unban a player.").queue();
            }
            //Author is host of the game, remove player
            else{
                Game game = games.get(channel.getIdLong());
                String idString = args[1].substring(3, args[1].length() - 1);
                try{
                    PlayerListStatus result = game.pardonPlayer(Long.parseLong(idString));
                    switch (result){
                        case successful -> channel.sendMessage("Successfully unbanned " + args[1]).queue();
                        case containsNot -> channel.sendMessage(args[1] + " is not banned").queue();
                        default -> channel.sendMessage("Something went wrong.").queue();
                    }
                }
                catch (Exception e){
                    channel.sendMessage("Unban failed. Mention the user you want to ban.").queue();
                }
            }
        }
        else{
            channel.sendMessage(event.getAuthor().getAsMention() + " there is no game in this channel.").queue();
        }
        return true;
    }
}
