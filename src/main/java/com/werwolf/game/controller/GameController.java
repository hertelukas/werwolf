package com.werwolf.game.controller;

import com.werwolf.game.*;
import com.werwolf.helpers.DayTextCreator;
import com.werwolf.helpers.IntroTextCreator;
import com.werwolf.helpers.NightTextCreator;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    long dayTime = 300000;
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
            gameFinished();
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
            gameFinished();
            return false;
        }
    }

    private void gameFinished() {
        EmbedBuilder finishedBuilder = new EmbedBuilder();
        if (status == GameStatus.WolfWin) {
            finishedBuilder.setTitle(UserMessageCreator.getCreator().getMessage(game, "wolf-win-title"));
            finishedBuilder.setThumbnail("https://cdn.pixabay.com/photo/2013/07/13/14/02/wolf-161987_960_720.png");
            finishedBuilder.setDescription(UserMessageCreator.getCreator().getMessage(game, "wolf-win-text"));
        } else {
            finishedBuilder.setTitle(UserMessageCreator.getCreator().getMessage(game, "villager-win-title"));
            //TODO Opensource checken
            finishedBuilder.setThumbnail("https://i.pinimg.com/originals/2c/9c/26/2c9c269156e26259dbe8f4733249f9b5.jpg");
            finishedBuilder.setDescription(UserMessageCreator.getCreator().getMessage(game, "villager-win-text"));
        }
        game.getChannel().sendMessage(finishedBuilder.build()).queue();

        //Rollen aller Spieler ausgeben
        EmbedBuilder roleoutBuilder = new EmbedBuilder().setDescription(UserMessageCreator.getCreator().getMessage(game, "role-sout"));
        Map<CharacterType, List<Player>> groups = game.getPlayers().stream().sorted(Comparator.comparingInt(p -> p.getCharacterType().ordinal())).collect(Collectors.groupingBy(Player::getCharacterType));
        for (Map.Entry<CharacterType, List<Player>> playerEntry : groups.entrySet()) {
            StringBuilder groupSB = new StringBuilder();
            for (Player player : playerEntry.getValue()) {
                groupSB.append(player.getUsername()).append("\r");
            }
            roleoutBuilder.addField(playerEntry.getKey().toString(), groupSB.toString(), false);
        }
        game.getChannel().sendMessage(roleoutBuilder.build()).queue();

        game.stop();
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

    public String getRandomStory(boolean isNight) {
        if(isNight)
            return NightTextCreator.getCreator().getStory(game, 0);
        else
            return NightTextCreator.getCreator().getStory(game, 0);

    }

    private String getRandomIntro() {
        return IntroTextCreator.getCreator().getStory(game, 0);
    }

    public void sendIntroMessage() {
        EmbedBuilder introBuilder = new EmbedBuilder();
        if (game.getTumMode()) {
            introBuilder.setTitle(UserMessageCreator.getCreator().getMessage(game, "title")).setDescription(getRandomIntro()).setThumbnail("https://cdn.discordapp.com/attachments/820378239821676616/821080884324990996/Kondom.jpg");
        } else {
            introBuilder.setTitle(UserMessageCreator.getCreator().getMessage(game, "title")).setDescription(getRandomIntro()).setThumbnail("https://cdn.pixabay.com/photo/2020/12/28/14/31/wolf-5867343_960_720.png");

        }
        game.getChannel().sendMessage(introBuilder.build()).queue();
    }
}