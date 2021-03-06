package com.werwolf.game.controller;

import com.werwolf.game.Game;
import com.werwolf.game.roles.Player;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MayorVotingController {

    private final static Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private final GameController gameController;
    private Game game;
    private long votingmessageID = -1;
    private boolean isVoting = false;
    private int votesNeeded = 0;
    private HashMap<Player, Integer> votingmap = new HashMap<>();
    private HashMap<String, Player> targetmap = new HashMap<>();
    private List<Player> alreadyVotet;
    private boolean firstVoting;

    public MayorVotingController(GameController gameController) {
        this.gameController = gameController;
    }

    public void newVoting(boolean firstVoting, Player major) {
        this.firstVoting = firstVoting;
        this.game = gameController.game;
        isVoting = true;
        votesNeeded = (firstVoting ? game.getPlayers().size() : 1);
        votingmap = new HashMap<>();
        targetmap = new HashMap<>();
        alreadyVotet = new ArrayList<>();

        if (!firstVoting) normalVoting(major);
        else firstVoting();
    }

    private void normalVoting(Player major) {
        StringBuilder playerSb = new StringBuilder();
        EmbedBuilder votingMessageBuilder = new EmbedBuilder();

        char prefix = 'A';

        for (Player p : game.getPlayers()) {
            if (p.isAlive()) playerSb.append(prefix++).append(": ").append(p.getUsername()).append("\r");
        }

        votingMessageBuilder.setTitle(UserMessageCreator.getCreator().getMessage(game, "mayor-election"))
                .addField(UserMessageCreator.getCreator().getMessage(game, "mayor-candidates"), playerSb.toString(), true);

        major.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(votingMessageBuilder.build()).queue(message -> {
            votingmessageID = message.getIdLong();
            int unicodeStart = 0xDDE6;
            int i = 0;
            for (Player p : game.getPlayers()) {
                if (p.isAlive()) {
                    message.addReaction("\uD83c" + (char) (unicodeStart + i)).queue();
                    targetmap.put("\uD83c" + (char) (unicodeStart + i), p);
                    i++;
                }
            }
        }));

        votesNeeded = 1;
        LOGGER.info("Auf voting Ergebnis warten");
    }

    private void firstVoting() {
        StringBuilder playerSb = new StringBuilder();
        EmbedBuilder votingMessageBuilder = new EmbedBuilder();

        char prefix = 'A';

        for (Player p : game.getPlayers()) {
            if (p.isAlive()) playerSb.append(prefix++).append(": ").append(p.getUsername()).append("\r");
        }

        votingMessageBuilder.setTitle(UserMessageCreator.getCreator().getMessage(game, "mayor-election"))
                .addField(UserMessageCreator.getCreator().getMessage(game, "mayor-candidates"), playerSb.toString(), true)
                .setThumbnail("https://media.discordapp.net/attachments/821717465679003670/822606161710809088/ballot-1294935_1280.png?width=667&height=679");

        game.getChannel().sendMessage(votingMessageBuilder.build()).queue(message -> {
            votingmessageID = message.getIdLong();
            int unicodeStart = 0xDDE6;
            int i = 0;
            for (Player p : game.getPlayers()) {
                message.addReaction("\uD83c" + (char) (unicodeStart + i)).queue();
                targetmap.put("\uD83c" + (char) (unicodeStart + i), p);
                i++;
            }
        });

        votesNeeded = game.getPlayers().size();
        LOGGER.info("Auf voting Ergebnis warten");
    }

    void receiveVoting(Player player, String target) {
        if (!isVoting) return;

        Player pTarget = targetmap.get(target);
        votesNeeded--;

        if (alreadyVotet.contains(player)) {
            return;
        } else {
            alreadyVotet.add(player);
        }

        LOGGER.info(player.getUsername() + " hat f??r " +  targetmap.get(target).getUsername() + " gestimmt");

        if (votingmap.containsKey(player)) {
            votingmap.computeIfPresent(pTarget, (p, val) -> val = val + 1);
        } else {
            votingmap.put(pTarget, 1);
        }
        if (votesNeeded == 0) {
            votingResult();

            if (firstVoting) {
                LOGGER.info("Erste Nacht gestartet");
                gameController.nextNight();
            } else  if (gameController.isNight) {
                gameController.nextDay();
            } else if (!gameController.isNight) {
                gameController.nextNight();
            }
        }
    }

    void votingResult() {
        Map.Entry<Player, Integer> major = null;
        for (Map.Entry<Player, Integer> p : votingmap.entrySet()) {
            if (major == null) major = p;

            LOGGER.info(major.getKey().getUsername() + " hat " + major.getValue() + " Stimmen");
            if (major.getValue() < p.getValue()) major = p;
        }

        EmbedBuilder votingResultBuilder = new EmbedBuilder();
        votingResultBuilder.setTitle(UserMessageCreator.getCreator().getMessage(game, "mayor-new"));

        if (major == null) {
            votingResultBuilder.setDescription(game.getPlayers().get(0) + UserMessageCreator.getCreator().getMessage(game, "mayor-result"));
            game.getChannel().sendMessage(votingResultBuilder.build()).queue();
            game.getPlayers().get(0).setMajor(true);
        } else {
            votingResultBuilder.setDescription(major.getKey().getUsername() + UserMessageCreator.getCreator().getMessage(game, "mayor-result"));
            game.getChannel().sendMessage(votingResultBuilder.build()).queue();
            major.getKey().setMajor(true);
        }
    }

    public long getVotingmessageID() {
        return votingmessageID;
    }

    public boolean isVoting() {
        return isVoting;
    }

    public boolean isFirstVoting() {
        return firstVoting;
    }
}
