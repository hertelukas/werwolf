package com.werwolf.game.Controler;

import com.werwolf.game.*;

public class GameController {
    boolean isActive;
    boolean isNight;
    Game game;
    long wolfVoteTime;
    long dayTime; // oder voteTime (muss noch evtl geklaert werden)
    NightController nightController;
    DayController dayController;
    private VotingController votingController = new VotingController(this);

    public GameController(Game game) {
        this.game = game;
        this.isActive = false;
        nightController = new NightController(game, wolfVoteTime);
        dayController = new DayController(game, dayTime);
        //TODO wereWolfVoteTime und voteTime standard festlegen
    }

    public boolean nextDay() {
        //TODO
        isNight = false;
        // if (gameStatus() == GameStatus.Cont)
            dayController.startDay();
        return false;
    }

    public boolean nextNight() {
        //TODO
        isNight = true;
        nightController.startNight();
        return false;
    }

    public GameStatus gameStatus() {
        long werewolves = game.getPlayers().stream().filter(p -> p.getCharacterType() == CharacterType.Werewolf).count();
        long alive = game.getPlayers().stream().filter(Player::isAlive).count();
        if (werewolves == 0)
            return GameStatus.VillagerWin;
        else if (werewolves >= (alive - werewolves))
            return GameStatus.WolfWin;
        else
            return GameStatus.Cont;
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

    public VotingController getVotingController() {
        return votingController;
    }

    public boolean isVoting() {
        if (isNight) {
            return nightController.isVotingTime();
        } else {
            return dayController.isVotingTime();
        }
    }

    public void setVoting(boolean isVotingTime) {
        if (isNight) {
            nightController.setVotingTime(isVotingTime);
        } else {
            dayController.setVotingTime(isVotingTime);
        }
    }

    public Long getVotingMessage() {
        if (isNight) {
            return nightController.getVotingMessageID();
        } else {
            return dayController.getVotingMessageID();
        }
    }

    public void continueAfterVoting() {
        if (isNight) {
            nightController.continueAfterVoting();
        } else {
            //TODO
        }
    }
}
