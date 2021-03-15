package com.werwolf.game.Controler;

import com.werwolf.game.Day;
import com.werwolf.game.Game;
import com.werwolf.game.Player;

import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class DayController {

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
        //todo
        List<Player> lastNight = game.getController().nightController.nights.peek().getAlive();
        List<Player> currAlive = game.getPlayers().stream().filter(Player::isAlive).collect(Collectors.toList());
        days.add(new Day(currAlive, game.getTumMode()));

        Collection<Player> killedDuringNight = CollectionUtils.subtract(lastNight, currAlive);

        StringBuilder storySb = new StringBuilder();
        storySb.append("Der").append(days.size()).append(". Tag beginnt, in der Nacht");
        if(killedDuringNight.size() == 0)
            storySb.append(" wurde niemand getötet");
        else {
            storySb.append(killedDuringNight.size() > 1 ? " wurden " : " wurde ");
            for (Iterator<Player> it = killedDuringNight.iterator(); it.hasNext(); ) {
                Player p = it.next();
                storySb.append(p.getUsername());
                if (it.hasNext()) {
                    storySb.append(", ");
                }
            }
            storySb.append(" getötet.");
        }

        EmbedBuilder storyBuilder = new EmbedBuilder();
        storyBuilder.setTitle(days.size() + ". Tag");
        storyBuilder.setDescription(storySb);
        // storyBuilder.setThumbnail(); find picture
        game.getChannel().sendMessage(storyBuilder.build()).queue();

        createVoting();

        // Update Tag Objekt mit Voting stats

        // begin night

    }

    private void createVoting() {
        List<Player> alive = days.peek().getAlive();
        StringBuilder playerSb = new StringBuilder();
        EmbedBuilder votingMessageBuilder = new EmbedBuilder();
        game.getVotingController().newVoting(true);

        char base = 'A';

        for (Player p : alive) {
            playerSb.append(base++).append(": ").append(p.getUsername()).append("\r");
        }

        votingMessageBuilder.setTitle("Voting").addField("Lebende Spieler", playerSb.toString(), true);

        // Thread.sleep()?

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

