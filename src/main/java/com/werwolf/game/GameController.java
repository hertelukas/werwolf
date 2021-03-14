package com.werwolf.game;

public class GameController {
    boolean isActive;
    boolean isNight;
    Game game;
    long wolfVoteTime;
    long dayTime; // oder voteTime (muss noch evtl geklaert werden)
    NightController nightController;
    DayController dayController;

    public GameController(Game game) {
        this.game = game;
        this.isActive = false;
        nightController = new NightController(game, wolfVoteTime);
        dayController = new DayController(game);
        //TODO wereWolfVoteTime und voteTime standard festlegen
    }

    boolean nextDay() {
        //TODO
        dayController.startDay();
        return false;
    }

    boolean nextNight() {
        //TODO
        nightController.startNight();
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
