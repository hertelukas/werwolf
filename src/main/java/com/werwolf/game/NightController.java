package com.werwolf.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
        wolfVoteMsg.setTitle("Who should be killed?");

        channel.sendMessage("").queue();
        try {
            TimeUnit.MILLISECONDS.sleep(voteTime);
        } catch (InterruptedException ignored) {
        }

        // unmute all players

    }

    // first night?

}
