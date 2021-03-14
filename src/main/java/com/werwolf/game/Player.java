package com.werwolf.game;

public class Player {
    private String username;
    private long id;
    private boolean isAlive;

    public Player(String username, long id) {
        this.username = username;
        this.id = id;
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
