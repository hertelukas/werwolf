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
    boolean savedByBodyguard = false;
    Guild guild;
    boolean isMajor = false;
    private boolean hasSex = false;
    boolean hasVoted = false;
    Prostitute whore = null;
    boolean isAttacked = false;

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

    public boolean isSavedByBodyguard() {
        return savedByBodyguard;
    }

    public void setSavedByBodyguard(boolean savedByBodyguard) {
        this.savedByBodyguard = savedByBodyguard;
    }

    public boolean isMajor() {
        return isMajor;
    }

    public void setMajor(boolean major) {
        isMajor = major;
    }
    public boolean isHavingSex() {
        return hasSex;
    }

    public void setHasSex(boolean hasSex){
        this.hasSex = hasSex;
    }


    /**
     * Versucht den Spieler umzubringen falls m??glich true sonst false
     */
    public boolean die(Game game) {
        if (!isSavedByBodyguard()) {
            if (whore != null) whore.dieSure(game);
            this.isAlive = false;
            if (game.getTumMode()) sendMessage("https://bit.ly/unexzellent");

            if (game.getConfigurations().isShowRole()) {
                EmbedBuilder showRole = new EmbedBuilder();
                showRole.setTitle(getUsername()).setDescription(getUsername() + UserMessageCreator.getCreator().getMessage(game, "death-Message") + characterType);

                game.getChannel().sendMessage(showRole.build()).queue();
            }
            return true;
        } else {
            isAttacked = true;
            LOGGER.info("Er wurde vom Bodyguard besch??tzt");
            return false;
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
     * Gibt zur??ck, ob der ausgew??hlte Spieler voten kann oder nicht (da er z.B. im Gef??ngnis ist)
     * @return boolean ob er voten kann
     */
    public boolean canVote() {
        if (!jailed) {
            return characterType.canVote();
        } else {
            LOGGER.info("Er kann nicht voten, er wurde gejailt");
            return false;
        }
    }

    /**
     * Jeder Player hat die Methode Vote, es wird erst gecheckt, ob dieser Spieler ??berhaupt voten kann, wenn ja dann votet er, wenn nein votet er nicht
     * @param target Spieler der als Ziel des m??glichen Votings ausgew??hlt wurde
     * @param votings Hashmap, die die Voting der Werew??lfe beinhaltet (Falls sp??ter auch noch andere darauf zugriff haben sollen)
     * @param game das Spiel lmao
     */
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
    }

    /**
     * Setzt die ggf. gesetzen Attribute/durch den Charackter ausgel??sten Aktionen zur??ck beim Aufruf
     * (Wird jede Runde f??r die in der Runde am Anfang lebenden Spieler aufgerufen
     * @param game das Spiel lmao
     */
    public void reset(Game game) {
    }

    /**
     * Erm??glicht es dem Spieler in der n??chsten Runde wieder zu voten, falls er diese Runde gejailt war oder ??hnliches
     */
    public void setCanVoteTrue(Game game) {
        if (jailed) {
            sendMessage(UserMessageCreator.getCreator().getMessage(game, "jailor-jails"));
            jailed = false;
        }
    }

    public boolean hasVoted() {
        return hasVoted;
    }
}