package com.werwolf.game.controller;

import com.werwolf.core.handler.AudioHandler;
import com.werwolf.game.*;
import com.werwolf.game.roles.CharacterType;
import com.werwolf.game.roles.Player;
import com.werwolf.helpers.NightTextCreator;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class NightController {
    private final static Logger LOGGER = LoggerFactory.getLogger(NightController.class);

    private final Game game;
    private final long voteTime;
    Stack<Night> nights = new Stack<>(); // Update am Anfang oder Ende der Nacht?
    private boolean votingTime = false;
    private long votingMessageID;

    public NightController(Game game, long wolfVoteTime) {
        this.game = game;
        voteTime = wolfVoteTime;
    }

    void startNight() {
        LOGGER.info("Nacht startet");

        //Fügt die neue Nacht dem Stackhinzu
        if (nights.isEmpty())
            nights.add(new FirstNight(game.getPlayers().stream().filter(Player::isAlive).collect(Collectors.toList()), game));
        else
            nights.add(new Night(game.getPlayers().stream().filter(Player::isAlive).collect(Collectors.toList()), game));

        game.setWerwolfWritePermissions(true);

        //Storytime
        StringBuilder storySB = new StringBuilder();
        storySB.append(NightTextCreator.getCreator().getStory(game, nights.size()));
        EmbedBuilder storyBuilder = new EmbedBuilder();
        storyBuilder.setTitle(nights.size() + ". " + UserMessageCreator.getCreator().getMessage(game, "night"));
        storyBuilder.setDescription(storySB);
        if (game.getTumMode())
            storyBuilder.setThumbnail("https://cdn.pixabay.com/photo/2017/01/18/12/33/session-1989711_960_720.png");
        else storyBuilder.setThumbnail("https://cdn.pixabay.com/photo/2016/11/29/13/12/cloudy-1869753_960_720.jpg");
        game.getChannel().sendMessage(storyBuilder.build()).queue();

        //Voting Time
        LOGGER.info("Voting startet");
        createVoting();

    }

    void continueAfterVoting() {
        LOGGER.info("Voting beendet");
        LOGGER.info("Rollen werden resettet");
        for (Player player : nights.peek().getAlive()) {
            player.reset(game);
        }

        updateVotingResult();
        game.getController().gameStatus();
        game.getController().nextDay();
        //Nacht Objekt mit Daten updaten (wie viele für wen gevotet haben etc.)

        //Tag bricht an
    }

    private void updateVotingResult() {

        //Nachricht wird geupdatet
        char prefix = 'A';
        StringBuilder playerSB = new StringBuilder();
        Night currentnight = game.getController().getNightController().getNights().peek();
        EmbedBuilder votingMessageBuilder = new EmbedBuilder();

        for (Player player : currentnight.getAlive()) {
            playerSB.append(prefix++).append(": ").append(player.getUsername());
            if (nights.peek().getDiedtonight().contains(player)) {
                playerSB.append("  🗡🩸");
            }
            playerSB.append("\r");
        }

        AudioHandler.getAudioHandler().loadAndPlay(game, "Drums.wav", false, true);
        AudioHandler.getAudioHandler().loadAndPlay(game, "Werwolf3.wav", true, false);

        votingMessageBuilder.setTitle(UserMessageCreator.getCreator().getMessage(game, "vote-title")).addField(UserMessageCreator.getCreator().getMessage(game, "vote-results"), playerSB.toString(), true);
        if (game.getTumMode()) {
            votingMessageBuilder.setThumbnail("https://cdn.discordapp.com/attachments/820378239821676616/821080486741934110/image0.png");
        } else {
            votingMessageBuilder.setThumbnail("https://cdn.pixabay.com/photo/2013/07/13/12/32/tombstone-159792_960_720.png");
        }
        game.getChannel().retrieveMessageById(game.getCurrentVotingMessage()).queue(message -> {
            message.editMessage(votingMessageBuilder.build()).queue();
            message.clearReactions().queue();
        });

    }

    private void notifyBodyguard(Player savedPlayer) {
        for (Player player : game.getPlayers()) {
            if(player.getCharacterType() == CharacterType.Bodyguard)
                player.sendMessage(savedPlayer.getUsername() + UserMessageCreator.getCreator().getMessage(game, "bodyguard-save"));
        }

    }


    private void createVoting() {
        StringBuilder playerSB = new StringBuilder();
        EmbedBuilder votingMessageBuilder = new EmbedBuilder();
        game.getVotingController().newVoting(true);

        char prefix = 'A';

        for (Player player : nights.peek().getAlive()) {
            playerSB.append(prefix++).append(": ").append(player.getUsername()).append("\r");
        }

        votingMessageBuilder.setTitle(UserMessageCreator.getCreator().getMessage(game, "vote-title")).addField(UserMessageCreator.getCreator().getMessage(game, "living-players"), playerSB.toString(), true);
        if (game.getTumMode()) votingMessageBuilder.setThumbnail("https://www.gerassist.com/wp-content/uploads/2020/02/tum-logo.png");
        //TODO ggf. warteZeit verändern/entfernen
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        game.getChannel().sendMessage(votingMessageBuilder.build()).queue(message -> {
            int unicodeStart = 0xDDE6;
            for (int i = 0; i < nights.peek().getAlive().size(); i++) {
                message.addReaction("\uD83c" + (char) (unicodeStart + i)).queue();
                //Ins Voting hinzufügen
                game.getVotingController().addPlayer("\uD83c" + (char) (unicodeStart + i), nights.peek().getAlive().get(i).getId());
            }
            votingMessageID = message.getIdLong();
        });

        //Lebende Spieler an den Werewolf-channel schicken
        EmbedBuilder werewolfMessage = new EmbedBuilder().setTitle(UserMessageCreator.getCreator().getMessage(game,  "wolf-information"));
        StringBuilder livingPlayerSB = new StringBuilder();
        StringBuilder livingWerewolfsSB = new StringBuilder();
        for (Player player : nights.peek().getAlive()) {
            if (player.getCharacterType().isCanSeeWWChannel()) {
                livingWerewolfsSB.append(player.getUsername()).append("\r");
            } else {
                livingPlayerSB.append(player.getUsername()).append("\r");
            }
        }
        werewolfMessage.addField(UserMessageCreator.getCreator().getMessage(game, "living-players"), livingPlayerSB.toString(), false);
        werewolfMessage.addField(UserMessageCreator.getCreator().getMessage(game, "living-wolves"), livingWerewolfsSB.toString(), false);

        game.sendToWerewolfChannel(werewolfMessage.build());

        votingTime = true;
    }

    // Getter/Setter
    public boolean isVotingTime() {
        return votingTime;
    }

    public long getVotingMessageID() {
        return votingMessageID;
    }

    public void setVotingTime(boolean votingTime) {
        this.votingTime = votingTime;
    }

    public Stack<Night> getNights() {
        return nights;
    }
}
