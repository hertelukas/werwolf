package com.werwolf.game.Controler;

import com.werwolf.game.*;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URI;

public class GameController {
    private static final int DAY_STORY_AMOUNT = 1;
    private static final int DAY_TUM_STORY_AMOUNT = 1;
    private static final int NIGHT_STORY_AMOUNT = 2;
    private static final int NIGHT_TUM_STORY_AMOUNT = 1;
    boolean isActive;
    boolean isNight;
    Game game;
    long wolfVoteTime;
    long dayTime; // oder voteTime (muss noch evtl geklaert werden)
    NightController nightController;
    DayController dayController;
    private VotingController votingController = new VotingController(this);
    GameStatus status = GameStatus.Cont;

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
        GameStatus status;
        if (werewolves == 0)
            status = GameStatus.VillagerWin;
        else if (werewolves >= (alive - werewolves))
            status = GameStatus.WolfWin;
        else
            status = GameStatus.Cont;
        this.status = status;
        return status;
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
            dayController.continueAfterVoting();
        }
    }

    public void endgame() {
        switch(status) {
            case WolfWin -> System.out.println("yeet");
            case VillagerWin -> System.out.println("bruh");
            default -> System.out.println("no bueno.");
        }
    }

    public static String getRandomStory(boolean tumMode, boolean isNight) {
        String story = "ERROR";
        try {
            File file = null;
            if (isNight) {
                file = new File(new URI("src/main/resources/NightStories.xml").toString());
            } else {
                file = new File(new URI("src/main/resources/DayStories.xml").toString());
            }
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            int randy = (int) (Math.random() * (tumMode ? (isNight ? NIGHT_TUM_STORY_AMOUNT : DAY_TUM_STORY_AMOUNT) : (isNight ? DAY_TUM_STORY_AMOUNT : DAY_STORY_AMOUNT)));
            story = document.getElementsByTagName("story" + (tumMode ? "TUM" : "") + randy).item(0).getTextContent();
        } catch (Exception e) {
            return "ERROR";
        }
        return story;
    }
}
