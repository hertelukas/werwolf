package com.werwolf.game.Controler;

import com.werwolf.game.*;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URI;

public class GameController {


    private final static Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private static final int DAY_STORY_AMOUNT = 1;
    private static final int DAY_TUM_STORY_AMOUNT = 1;
    private static final int NIGHT_STORY_AMOUNT = 2;
    private static final int NIGHT_TUM_STORY_AMOUNT = 1;
    private static final int INTRO_TUM_STORY_AMOUNT = 1;
    private static final int INTRO_STORY_AMOUNT = 1;
    boolean isActive;
    boolean isNight;
    Game game;
    long wolfVoteTime = 60000;
    long dayTime = 300000; // oder voteTime (muss noch evtl geklaert werden)
    NightController nightController;
    DayController dayController;
    private final VotingController votingController = new VotingController(this);
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
        if (status == GameStatus.Cont) {
            votingController.newVoting(false);
            isNight = false;
            dayController.startDay();
            return true;
        } else {
            game.getChannel().sendMessage("Spiel fertig" +  status.toString()).queue();
            game.stop();
            return false;
        }
    }

    public boolean nextNight() {
        //TODO
        if (status == GameStatus.Cont) {
            votingController.newVoting(true);
            isNight = true;
            nightController.startNight();
            return true;
        } else {
            game.getChannel().sendMessage("Spiel fertig: " + status.toString()).queue();
            game.stop();
            return false;
        }
    }

    public GameStatus gameStatus() {
        long werewolves = game.getPlayers().stream().filter(p -> p.getCharacterType() == CharacterType.Werewolf && p.isAlive()).count();
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

    public void setDayTime(long dayTime) {
        this.dayTime = dayTime;
    }

    public void setWolfVoteTime(long wolfVoteTime) {
        this.wolfVoteTime = wolfVoteTime;
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
            case WolfWin -> LOGGER.info("WOLFS WIN");
            case VillagerWin -> LOGGER.info("VILLAGER WIN");
            default -> LOGGER.info("Bis jetzt hat noch keiner gewonnen");
        }
    }

    public static String getRandomStory(boolean tumMode, boolean isNight) {
        String story;
        try {
            File file;
            if (isNight) {
                file = new File(new URI("src/main/resources/NightStories.xml").toString());
            } else {
                file = new File(new URI("src/main/resources/DayStories.xml").toString());
            }
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            int randy = (int) (Math.random() * (tumMode ? (isNight ? NIGHT_TUM_STORY_AMOUNT : NIGHT_STORY_AMOUNT) : (isNight ? DAY_TUM_STORY_AMOUNT : DAY_STORY_AMOUNT)));
            story = document.getElementsByTagName("story" + (tumMode ? "TUM" : "") + randy).item(0).getTextContent();
        } catch (Exception e) {
            return "ERROR";
        }
        return story;
    }

    public static String getRandomIntro(boolean tumMode) {
        String story;
        try {
            File file = new File(new URI("src/main/resources/IntroStories.xml").toString());

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            int randy = (int) (Math.random() * (tumMode ? INTRO_TUM_STORY_AMOUNT : INTRO_STORY_AMOUNT));
            story = document.getElementsByTagName("story" + (tumMode ? "TUM" : "") + randy).item(0).getTextContent();
        } catch (Exception e) {
            return "ERROR";
        }
        return story;
    }

    public void sendIntroMessage() {
        EmbedBuilder introBuilder = new EmbedBuilder();
        if (game.getTumMode()) {
            introBuilder.setTitle("TUMWOLF").setDescription(getRandomIntro(true)).setThumbnail("https://cdn.discordapp.com/attachments/820378239821676616/821080884324990996/Kondom.jpg");
        } else {
            introBuilder.setTitle("Werewolf").setDescription(getRandomIntro(false)).setThumbnail("https://cdn.pixabay.com/photo/2020/12/28/14/31/wolf-5867343_960_720.png");

        }
        game.getChannel().sendMessage(introBuilder.build()).queue();
    }
}
