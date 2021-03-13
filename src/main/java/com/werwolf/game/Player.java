package com.werwolf.game;

public abstract class Player {
    private String username;
    private long id;
    private boolean isAlive;


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
