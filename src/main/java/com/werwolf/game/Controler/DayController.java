package com.werwolf.game.Controler;

import com.werwolf.game.Day;
import com.werwolf.game.Game;
import com.werwolf.game.Player;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
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
        storySb.append("Der").append(days.size()).append(". Tag beginnt, ");

        // x Person wurde getötet
    }
}
