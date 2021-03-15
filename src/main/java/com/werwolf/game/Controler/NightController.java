package com.werwolf.game.Controler;

import com.werwolf.game.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NightController {

    Game game;
    long voteTime;
    Stack<Night> nights = new Stack<>(); // Update am Anfang oder Ende der Nacht?

    public NightController(Game game, long wolfVoteTime) {
        this.game = game;
        voteTime = wolfVoteTime;
    }

    void startNight() {
        //Fügt die neue Nacht dem Stackhinzu
        if (nights.isEmpty()) nights.add(new FirstNight(game.getPlayers().stream().filter(player -> player.isAlive()).collect(Collectors.toList())));
        else nights.add(new Night(game.getPlayers().stream().filter(player -> player.isAlive()).collect(Collectors.toList())));

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

        //Voting durchführen

        //Voting auswerten

        //Nacht Objekt mit Daten updaten (wie viele für wen gevotet haben etc.)

        //Tag bricht an









//        //todo
//
//        // mute all players in voice channel
//        EmbedBuilder wolfVoteMsg = new EmbedBuilder();
//        wolfVoteMsg.setTitle("who ded");
//        // scuffed but kinda works
//        List<Player> alive = game.getPlayers().stream().filter(Player::isAlive).collect(Collectors.toList());
//        for (int i = 0; i < alive.size(); i++)
//            wolfVoteMsg.addField(alive.get(i).getUsername(), ":regional_indicator_" + (char) ('a' + i), false);
//
//        channel.sendMessage(wolfVoteMsg.build()).queue(msg -> msg.addReaction("platzhalter lol").queue()); // fix this
//
//
//        try {
//            TimeUnit.MILLISECONDS.sleep(voteTime); // this waits, but checks jackshit -> need to check if wolf voting ended
//        } catch (InterruptedException ignored) {
//        }
//
//        // gather votes
//        // player.die()
//
//        // unmute all players

    }


    private void createVoting() {
        StringBuilder playerSB = new StringBuilder();
        EmbedBuilder votingMessageBuilder = new EmbedBuilder();

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
            }
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
    }

    // first night?

}