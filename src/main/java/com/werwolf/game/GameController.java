package com.werwolf.game;

public class GameController {
    private boolean isActive;
    private boolean isNight;
    private Game game;
    private int werewolfVoteTime;
    private int voteTime;

    public GameController(Game game) {
        this.game = game;
        this.isActive = false;
        //TODO wereolfVoteTime und voteTime standard festlegen
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

    public int getWerewolfVoteTime() {
        return werewolfVoteTime;
    }

    public void setWerewolfVoteTime(int werewolfVoteTime) {
        this.werewolfVoteTime = werewolfVoteTime;
    }

    public int getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(int voteTime) {
        this.voteTime = voteTime;
    }
}
