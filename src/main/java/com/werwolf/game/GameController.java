package com.werwolf.game;

public class GameController {
    boolean isActive;
    boolean isNight;
    Game game;
    long wolfVoteTime;
    long dayTime; // oder voteTime (muss noch evtl geklaert werden)

    public GameController(Game game) {
        this.game = game;
        this.isActive = false;
        //TODO wereolfVoteTime und voteTime standard festlegen
    }

    boolean nextDay() {
        //TODO
        return false;
    }

    boolean nextNight() {
        //TODO
        return false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
