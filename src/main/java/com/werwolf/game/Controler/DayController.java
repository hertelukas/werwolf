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

    public DayController(Game game, long voteTime) {
        this.game = game;
        this.voteTime = voteTime;
    }

    void startDay() {
        //todo
        List<Player> lastNight = game.getController().nightController.nights.peek().getAlive();
        List<Player> currAlive = game.getPlayers().stream().filter(Player::isAlive).collect(Collectors.toList());
        days.add(new Day(currAlive));

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

    }



        // x Person wurde getötet

}

