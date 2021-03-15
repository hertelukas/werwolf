package com.werwolf.game.Controler;

import com.werwolf.game.CharacterType;
import com.werwolf.game.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingController {

    private final static Logger LOGGER = LoggerFactory.getLogger(VotingController.class);

    private final GameController gameController;
    private HashMap<Long, Integer> votings = new HashMap<>();
    private HashMap<String, Long> playerPrefixmap = new HashMap<>();
    private boolean nightVoting = false;
    private List<Long> alreadyVoted = new ArrayList<>();


    public VotingController(GameController controller) {
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
            if (gameController.getGame().getPlayer(voter).isAlive() &&
                    gameController.getGame().getPlayer(voter).getCharacterType() == CharacterType.Werewolf) {

                if (gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)).getCharacterType() != CharacterType.Werewolf) {
                    votings.computeIfPresent(playerPrefixmap.get(playerPrefix), (aLong, integer) -> (integer = integer + 1));
                    alreadyVoted.add(voter);
                    LOGGER.info(gameController.getGame().getPlayer(voter).getUsername() + " hat für " + gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)) + " gestimmt");
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
            if (gameController.getGame().getPlayer(voter).isAlive()) {
                votings.computeIfPresent(playerPrefixmap.get(playerPrefix), (aLong, integer) -> (integer = integer + 1));
                alreadyVoted.add(voter);
                LOGGER.info(gameController.getGame().getPlayer(voter).getUsername() + " hat für " + gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)).getUsername() + " gestimmt");
            }

            for (Player player : gameController.getGame().getPlayers()) {
                if (player.isAlive()) {
                    if (!alreadyVoted.contains(player.getId())) {
                        finished = false;
                    }
                }
            }
        }

        if (finished) {
            StringBuilder loggingSB = new StringBuilder();
            for (Map.Entry<Long, Integer> entry : votings.entrySet()) {
                loggingSB.append(gameController.getGame().getPlayer(entry.getKey()) + " hat " + entry.getValue() + "Stimmen\r");
            }
            LOGGER.info(loggingSB.toString());

            gameController.setVoting(false);
            gameController.continueAfterVoting();
        }


    }

    public HashMap<Long, Integer> getResult() {
        return votings;
    }
}

