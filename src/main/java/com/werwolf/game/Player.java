package com.werwolf.game;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class Player {
    private String username;
    private long id;
    private boolean isAlive;

    //Todo we might want to remove this, makes no sense. Is here to allow Werewolf and Villager with no constructor
    public Player(){};

    public Player(long id, String username){
        this.id = id;
        this.username = username;
    }

    public Player(User user){
        this.username = user.getName();
        this.id = user.getIdLong();
    }

    public Player(Member member) {
        if(member.getNickname() == null || member.getNickname().isEmpty())
            this.username = member.getUser().getName();
        else
            this.username = member.getNickname();

        this.id = member.getIdLong();
    }

    //Getter & Setter


    public String getUsername() {
        return username;
    }

    public long getId() {
        return id;
    }

    public boolean vote(Player player) {
        return true;
    }

    /**
     * Sendet eine private Nachricht an den Spieler
     * @param message Nachricht
     */
    public void sendMessage(String message) {

    }
}
