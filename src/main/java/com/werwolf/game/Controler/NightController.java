package com.werwolf.game.Controler;

import com.werwolf.game.*;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class NightController {

    private Game game;
    private long voteTime;
    Stack<Night> nights = new Stack<>(); // Update am Anfang oder Ende der Nacht?
    private boolean votingTime = false;
    private long votingMessageID;

    public NightController(Game game, long wolfVoteTime) {
        this.game = game;
        voteTime = wolfVoteTime;
    }

    void startNight() {
        //Fügt die neue Nacht dem Stackhinzu
        if (nights.isEmpty()) nights.add(new FirstNight(game.getPlayers().stream().filter(Player::isAlive).collect(Collectors.toList()), game.getTumMode()));
        else nights.add(new Night(game.getPlayers().stream().filter(Player::isAlive).collect(Collectors.toList()), game.getTumMode()));

        //Storytime
        StringBuilder storySB = new StringBuilder();
        storySB.append("Die ").append(nights.size()).append(". Nacht bricht an, ").append(nights.peek().getStory());
        EmbedBuilder storyBuilder = new EmbedBuilder();
        storyBuilder.setTitle(nights.size() + ". Nacht");
        storyBuilder.setDescription(storySB);
        storyBuilder.setThumbnail("https://cdn.pixabay.com/photo/2016/11/29/13/12/cloudy-1869753_960_720.jpg");
        game.getChannel().sendMessage(storyBuilder.build()).queue();

        //Voting Time
        createVoting();

    }

    void continueAfterVoting() {
        updateVotingResult(game.getVotingController().getResult());

        //Nacht Objekt mit Daten updaten (wie viele für wen gevotet haben etc.)

        //Tag bricht an
    }

    private void updateVotingResult(HashMap<Long, Integer> result) {
        //Voting auswerten
        result = game.getController().getVotingController().getResult();
        StringBuilder playerSB = new StringBuilder();
        EmbedBuilder votingMessageBuilder = new EmbedBuilder();

        char prefix = 'A';
        Map.Entry<Long, Integer> votedPlayer = null;
        for (Map.Entry<Long, Integer> player: result.entrySet()) {
            if (votedPlayer == null) votedPlayer = player;

            if (votedPlayer.getValue() < player.getValue()) votedPlayer = player;
        }

        for (Player player : nights.peek().getAlive()) {
            playerSB.append(prefix++ + ": ").append(player.getUsername());
            if (player.getId() == votedPlayer.getKey()) {
                playerSB.append("  🗡🩸");
            }

            playerSB.append("\r");

        }

        votingMessageBuilder.setTitle("Voting").addField("Voting Ergebnisse", playerSB.toString(), true).setThumbnail("https://cdn.pixabay.com/photo/2013/07/13/12/32/tombstone-159792_960_720.png");
        game.getChannel().retrieveMessageById(game.getCurrentVotingMessage()).queue(message -> {
            message.editMessage(votingMessageBuilder.build()).queue();
            message.clearReactions().queue();
        });
    }


    private void createVoting() {
        StringBuilder playerSB = new StringBuilder();
        EmbedBuilder votingMessageBuilder = new EmbedBuilder();
        game.getVotingController().newVoting(true);

        char prefix = 'A';

        for (Player player : nights.peek().getAlive()) {
            playerSB.append(prefix++ + ": ").append(player.getUsername()).append("\r");
        }

        votingMessageBuilder.setTitle("Voting").addField("Lebende Spieler", playerSB.toString(), true);
        //TODO ggf. warteZeit verändern/entfernen
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        game.getChannel().sendMessage(votingMessageBuilder.build()).queue(message -> {
            int unicodeStart = 0xDDE6;
            for (int i=0; i < nights.peek().getAlive().size(); i++) {
                message.addReaction("\uD83c" + (char) (unicodeStart + i)).queue();
                //Ins Votinghinzufügen
                game.getVotingController().addPlayer("\uD83c" + (char) (unicodeStart + i), nights.peek().getAlive().get(i).getId());
            }
            votingMessageID = message.getIdLong();
        });

        //Lebende Spieler an den Werewolfchannel schicken
        EmbedBuilder werewolfMessage = new EmbedBuilder().setTitle("Spielerinformation");
        StringBuilder livingPlayerSB = new StringBuilder();
        StringBuilder livingWerewolfsSB = new StringBuilder();
        for (Player player : nights.peek().getAlive()) {
            if (player.getCharacterType() == CharacterType.Villager) {
                livingPlayerSB.append(player.getUsername()).append("\r");
            } else {
                livingWerewolfsSB.append(player.getUsername()).append("\r");
            }
        }
        werewolfMessage.addField("Lebende Spieler", livingPlayerSB.toString(), false);
        werewolfMessage.addField("Lebende Werewölfe", livingWerewolfsSB.toString(), false);

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
}
