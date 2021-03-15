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

        boolean finished = true;

        if (nightVoting) {
            if (gameController.getGame().getPlayer(voter).getCharacterType() == CharacterType.Werewolf) {
                if (gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)).getCharacterType() != CharacterType.Werewolf) {
                    votings.computeIfPresent(playerPrefixmap.get(playerPrefix), (aLong, integer) -> (integer = integer + 1));
                    alreadyVoted.add(voter);
                    System.out.println(gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)).getUsername() + "wurden von " + gameController.getGame().getPlayer(voter).getUsername() + "gewählt!");
                }
            }


            //Check ob jeder Werewolf gevotet hat
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

        if (finished) {
            gameController.setVoting(false);
            gameController.continueAfterVoting();
        }


    }

    public HashMap<Long, Integer> getResult() {
        return votings;
    }
}

