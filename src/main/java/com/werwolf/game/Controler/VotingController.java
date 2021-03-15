package com.werwolf.game.Controler;

import com.werwolf.game.CharacterType;
import com.werwolf.game.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VotingController {
    private final GameController gameController;
    private HashMap<Long, Integer> votings = new HashMap<>();
    private HashMap<String, Long> playerPrefixmap = new HashMap<>();
    private boolean nightVoting = false;
    private List<Long> alreadyVoted = new ArrayList<>();


    public VotingController(GameController controller){
        this.gameController = controller;
    }

    public void newVoting(boolean isNight) {
        votings = new HashMap<>();
        playerPrefixmap = new HashMap<>();
        this.nightVoting = isNight;
        alreadyVoted = new ArrayList<>();
    }

    public void addPlayer(String playerPrefix, long playerID) {
        playerPrefixmap.put(playerPrefix, playerID);
        votings.put(playerID, 0);
    }

    public void vote(String playerPrefix, long voter) {

        if (nightVoting) {
            if (gameController.getGame().getPlayer(voter).getCharacterType() == CharacterType.Werewolf) {
                votings.computeIfPresent(playerPrefixmap.get(playerPrefix), (aLong, integer) -> (integer = integer + 1));
                alreadyVoted.add(voter);
            }


            //Check ob jeder Werewolf gevotet hat
            boolean finished = true;
            for (Player player : gameController.getGame().getPlayers()) {
                if (player.isAlive() && player.getCharacterType() == CharacterType.Werewolf) {
                    if (!alreadyVoted.contains(player.getId())) {
                        finished = false;
                    }
                }
            }


        } else {
            //TODO
        }

        if (gameController.getGame().getCurrentVotingMessage() != null) {

        }


    }
}

