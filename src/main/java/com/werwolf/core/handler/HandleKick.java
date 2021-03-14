package com.werwolf.core.handler;

import com.werwolf.game.Game;
import com.werwolf.game.PlayerListStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleKick extends MessageHandler{

    public HandleKick(){
        setName("Kick");
        setCommand("kick");
        setDescription("Kick a player");
    }
    @Override
    public boolean handle(GuildMessageReceivedEvent event, String command, String[] args) {
        if(!command.equals(getCommand())) return false;

        TextChannel channel = event.getChannel();

        if(games.containsKey(channel.getIdLong())){
            if(games.get(channel.getIdLong()).getHost().getId() != event.getAuthor().getIdLong()){
                channel.sendMessage(event.getAuthor().getAsMention() + " only the host can kick a player.").queue();
            }
            //Author is host of the game, remove player
            else{
                Game game = games.get(channel.getIdLong());
                String idString = args[1].substring(3, args[1].length() - 1);
                try {
                    long idToKick = Long.parseLong(idString);
                    if(idToKick == event.getAuthor().getIdLong()){
                        channel.sendMessage(event.getAuthor().getAsMention() + " you can't kick yourself.").queue();
                        return true;
                    }
                    PlayerListStatus result = game.removePlayer(idToKick);
                    switch (result){
                        case successful -> channel.sendMessage("Successfully kicked " + args[1]).queue();
                        case containsNot -> channel.sendMessage(args[1] + " is not in the game").queue();
                        default -> channel.sendMessage("Something went wrong").queue();
                    }
                }
                catch (Exception e){
                    channel.sendMessage("Kick failed. Mention the user you want to kick.").queue();
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
