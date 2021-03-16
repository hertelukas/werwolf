package com.werwolf.game.controller;

import com.werwolf.core.handler.AudioHandler;
import com.werwolf.game.Day;
import com.werwolf.game.Game;
import com.werwolf.game.Player;

import com.werwolf.helpers.DayTextCreator;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class DayController {
    private final static Logger LOGGER = LoggerFactory.getLogger(DayController.class);

    Game game;
    long voteTime;
    Stack<Day> days = new Stack<>();
    private boolean votingTime = false;
    private long votingMessageID;

    public DayController(Game game, long voteTime) {
        this.game = game;
        this.voteTime = voteTime;
    }

    void startDay() {
        LOGGER.info("Tag startet");

        List<Player> lastNight = game.getController().nightController.nights.peek().getAlive();
        List<Player> currAlive = game.getPlayers().stream().filter(Player::isAlive).collect(Collectors.toList());
        days.add(new Day(currAlive, game));

        Collection<Player> killedDuringNight = CollectionUtils.subtract(lastNight, currAlive);

        StringBuilder storySb = new StringBuilder();
        storySb.append(DayTextCreator.getCreator().getStory(game, days.size()));
        if(killedDuringNight.size() == 0)
            storySb.append(UserMessageCreator.getCreator().getMessage(game, "nobody-killed"));
        else {
            //todo only if german
            storySb.append(killedDuringNight.size() > 1 ? " wurden " : " wurde ");
            for (Iterator<Player> it = killedDuringNight.iterator(); it.hasNext(); ) {
                Player p = it.next();
                storySb.append(p.getUser().getAsMention());
                if (it.hasNext()) {
                    storySb.append(", ");
                }
            }
            storySb.append(UserMessageCreator.getCreator().getMessage(game, "killed"));
        }

        EmbedBuilder storyBuilder = new EmbedBuilder();
        storyBuilder.setTitle(days.size() + ". " + UserMessageCreator.getCreator().getMessage(game,"day"));
        storyBuilder.setDescription(storySb);
        storyBuilder.setThumbnail("https://cdn.pixabay.com/photo/2018/04/16/12/59/face-3324569_960_720.jpg"); // pls review somebody
        game.getChannel().sendMessage(storyBuilder.build()).queue();

        LOGGER.info("Voting startet");
        createVoting();
    }

    public void continueAfterVoting() {
        LOGGER.info("Voting beendet");
        updateVotingResult();
        game.getController().gameStatus();
        game.getController().nextNight();

    }

    public void updateVotingResult() {
        HashMap<Long, Integer> result = game.getController().getVotingController().getResult();
        StringBuilder playerSb = new StringBuilder();
        EmbedBuilder votingMessageBuilder = new EmbedBuilder();

        char prefix = 'A';
        Map.Entry<Long, Integer> votedPlayer = null;
        for(Map.Entry<Long, Integer> player : result.entrySet()) {
            if(votedPlayer == null || votedPlayer.getValue() < player.getValue())
                votedPlayer = player;
        }

        for(Player player : days.peek().getAlive()) {
            playerSb.append(prefix++).append(": ").append(player.getUsername());
            if (player.getId() == votedPlayer.getKey()) {
                player.die();
                playerSb.append("  🗡🩸");
                AudioHandler.getAudioHandler().loadAndPlay(game.getVoiceChannel(), "Betrugsversuch.wav", false, true);
                AudioHandler.getAudioHandler().loadAndPlay(game.getVoiceChannel(), "Never.mp3", true, false);
            }
            playerSb.append("\r");
        }


        votingMessageBuilder.setTitle(UserMessageCreator.getCreator().getMessage(game, "vote-title")).addField(UserMessageCreator.getCreator().getMessage(game, "vote-results"), playerSb.toString(), true);
        if (game.getTumMode()) {
            votingMessageBuilder.setThumbnail("https://cdn.discordapp.com/attachments/820378239821676616/821080486741934110/image0.png");
        } else {
            votingMessageBuilder.setThumbnail("https://cdn.discordapp.com/attachments/821091668974633080/821134542374961172/firsttime.jpeg");
        }
        game.getChannel().retrieveMessageById(game.getCurrentVotingMessage()).queue(message -> {
            message.editMessage(votingMessageBuilder.build()).queue();
            message.clearReactions().queue();
        });
    }

    private void createVoting() {
        List<Player> alive = days.peek().getAlive();
        StringBuilder playerSb = new StringBuilder();
        EmbedBuilder votingMessageBuilder = new EmbedBuilder();
        game.getVotingController().newVoting(false);

        char prefix = 'A';

        for (Player p : alive) {
            playerSb.append(prefix++).append(": ").append(p.getUsername()).append("\r");
        }

        votingMessageBuilder.setTitle("Voting").addField("Lebende Spieler", playerSb.toString(), true);

        // Thread.sleep()?
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        game.getChannel().sendMessage(votingMessageBuilder.build()).queue(message -> {
            int unicodeStart = 0xDDE6;
            for (int i=0; i < alive.size(); i++) {
                message.addReaction("\uD83c" + (char) (unicodeStart + i)).queue();
                //Ins Voting hinzufügen
                game.getVotingController().addPlayer("\uD83c" + (char) (unicodeStart + i), alive.get(i).getId());
            }
            votingMessageID = message.getIdLong();
        });
        votingTime = true;
    }


    public boolean isVotingTime() {
        return votingTime;
    }

    public long getVotingMessageID() {
        return votingMessageID;
    }

    public Player getPlayer(long playerID) {
        for (Player player : days.peek().getAlive()) {
            if (player.getId() == playerID) {
                return player;
            }
        }
        return null;
    }

    public void setVotingTime(boolean votingTime) {
        this.votingTime = votingTime;
    }
}