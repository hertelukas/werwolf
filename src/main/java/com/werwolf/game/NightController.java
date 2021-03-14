package com.werwolf.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class NightController {

    Game game;
    TextChannel channel;
    long voteTime;
    List<Night> nights;

    public NightController(Game game, long wolfVoteTime) {
        this.game = game;
        voteTime = wolfVoteTime;
        // channel = <jda oder so>.getTextChannelById(game.wolfChannelId);
    }

    void startNight() {
        //todo

        // mute all players in voice channel
        EmbedBuilder wolfVoteMsg = new EmbedBuilder();
        wolfVoteMsg.setTitle("who ded");
        List<Player> alive = game.getPlayers().stream().filter(Player::isAlive).collect(Collectors.toList());
        for (int i = 0; i < alive.size(); i++)
            wolfVoteMsg.addField(alive.get(i).getUsername(), ":regional_indicator_" + (char) ('a' + i), false);

        channel.sendMessage(wolfVoteMsg.build()).queue(msg -> msg.addReaction("platzhalter lol").queue()); // fix this


        try {
            TimeUnit.MILLISECONDS.sleep(voteTime);
        } catch (InterruptedException ignored) {
        }

        // gather votes
        // player.die()

        // unmute all players

    }

    // first night?

}
