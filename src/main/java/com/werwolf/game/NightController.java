package com.werwolf.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NightController {

    Game game;
    TextChannel channel;
    long voteTime;
    List<Night> nights; // Update am Anfang oder Ende der Nacht?

    public NightController(Game game, long wolfVoteTime) {
        this.game = game;
        voteTime = wolfVoteTime;
        // channel = <jda oder so>.getTextChannelById(game.wolfChannelId); how lol
    }

    void startNight() {
        //todo

        // mute all players in voice channel
        EmbedBuilder wolfVoteMsg = new EmbedBuilder();
        wolfVoteMsg.setTitle("who ded");
        // scuffed but kinda works
        List<Player> alive = game.getPlayers().stream().filter(Player::isAlive).collect(Collectors.toList());
        for (int i = 0; i < alive.size(); i++)
            wolfVoteMsg.addField(alive.get(i).getUsername(), ":regional_indicator_" + (char) ('a' + i), false);

        channel.sendMessage(wolfVoteMsg.build()).queue(msg -> msg.addReaction("platzhalter lol").queue()); // fix this


        try {
            TimeUnit.MILLISECONDS.sleep(voteTime); // this waits, but checks jackshit -> need to check if wolf voting ended
        } catch (InterruptedException ignored) {
        }

        // gather votes
        // player.die()

        // unmute all players

    }

    // first night?

}
