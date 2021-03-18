package com.werwolf.game.roles;

import com.werwolf.game.Game;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;

public class Player {

    private final static Logger LOGGER = LoggerFactory.getLogger(Player.class);

    String username;
    long id;
    boolean isAlive;
    User user;
    CharacterType characterType;
    boolean jailed;
    boolean savedByBodyguyard = false;
    Guild guild;

    public Player(User user, Guild guild){
        this.guild = guild;
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

    public void setCharacterType(CharacterType characterType) {
        this.characterType = characterType;
    }

    public boolean isSavedByBodyguyard() {
        return savedByBodyguyard;
    }

    public void setSavedByBodyguyard(boolean savedByBodyguyard) {
        this.savedByBodyguyard = savedByBodyguyard;
    }


    /**
     * Bringt den Spieler um
     */
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

    /**
     * Gibt zurück, ob der ausgewählte Spieler voten kann oder nicht (da er z.B. im Gefängnis ist)
     * @return boolean ob er voten kann
     */
    public boolean canVote() {
        if (!jailed) {
            return characterType.canVote();
        } else {
            return false;
        }
    }

    /**
     * Jeder Player hat die Methode Vote, es wird erst gecheckt, ob dieser Spieler überhaupt voten kann, wenn ja dann votet er, wenn nein votet er nicht
     * @param target Spieler der als Ziel des möglichen Votings ausgewählt wurde
     * @param votings Hashmap, die die Voting der Werewölfe beinhaltet (Falls später auch noch andere darauf zugriff haben sollen)
     */
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
    }

    /**
     * Ermöglicht es dem Spieler in der nächsten Runde wieder zu voten, falls er diese Runde gejailt war oder ähnliches
     */
    public void setCanVoteTrue(Game game) {
        if (jailed) {
            sendMessage(UserMessageCreator.getCreator().getMessage(game, "jailor-jails"));
            jailed = false;
        }
    }
}