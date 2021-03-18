package com.werwolf.game;

import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Consumer;

public class Player {

    private final static Logger LOGGER = LoggerFactory.getLogger(Game.class);

    String username;
    long id;
    boolean isAlive;
    User user;
    CharacterType characterType;
    private boolean jailed;
    private boolean savedByBodyguyard = false;

    //Todo we might want to remove this, makes no sense. Is here to allow Werewolf and Villager with no constructor
    public Player(){}

    public Player(User user, Guild guild){
        this.username = user.getName();
        this.id = user.getIdLong();
        this.user = user;
        this.characterType = CharacterType.Villager;
        this.isAlive = true;

        try{
            if(!Objects.requireNonNull(Objects.requireNonNull(guild.getMemberById(id)).getNickname()).isEmpty()){
                username = Objects.requireNonNull(guild.getMemberById(id)).getNickname();
            }
        }
        catch (Exception e){
            LOGGER.warn("Couldn't get nickname: " + e.getMessage());
        }
    }

    //Getter & Setter
    public CharacterType getCharacterType() {
        return characterType;
    }

    public String getUsername() {
        return username;
    }

    public long getId() {
        return id;
    }

    public boolean vote(Player player) {
        return true;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setJailed(boolean jailed) {
        this.jailed = jailed;
    }

    public boolean isJailed() {
        return jailed;
    }

    public User getUser() {
        return user;
    }

    public void die(Game game) {
        this.isAlive = false;

        if (game.getConfigurations().isShowRole()) {
            EmbedBuilder showRole = new EmbedBuilder();
            showRole.setTitle(getUsername()).setDescription(getUsername() + UserMessageCreator.getCreator().getMessage(game, "death-Message") + characterType);

            game.getChannel().sendMessage(showRole.build()).queue();
        }
    }

    /**
     * Sendet eine private Nachricht an den Spieler
     * @param message Nachricht
     */
    public void sendMessage(String message) {
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(message).queue());
    }

    /**
     * Sendet eine private Nachricht an den Spieler
     * @param embed Nachricht
     */
    public void sendMessage(MessageEmbed embed){
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(embed).queue());
    }

    public boolean canVote() {
        if (!jailed) {
            return characterType.canVote();
        } else {
            return false;
        }
    }

    public boolean isSavedByBodyguyard() {
        return savedByBodyguyard;
    }

    public void setSavedByBodyguyard(boolean savedByBodyguyard) {
        this.savedByBodyguyard = savedByBodyguyard;
    }
}