package com.werwolf.game;

import net.dv8tion.jda.api.entities.User;

public class Player {

    String username;
    long id;
    boolean isAlive;
    User user;
    CharacterType characterType;

    //Todo we might want to remove this, makes no sense. Is here to allow Werewolf and Villager with no constructor
    public Player(){}

    public Player(User user){
        this.username = user.getName();
        this.id = user.getIdLong();
        this.user = user;
        this.characterType = CharacterType.Villager;
        this.isAlive = true;
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

    public void die() {
        this.isAlive = false;
    }
    /**
     * Sendet eine private Nachricht an den Spieler
     * @param message Nachricht
     */
    public void sendMessage(String message) {
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(message).queue());
    }
}