package com.werwolf.game.controller;

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
        Player currVoter = gameController.getGame().getPlayer(voter); // der Dude, der gevotet hat

        if (nightVoting) {
            if (currVoter.isAlive()) {
                if (currVoter.getCharacterType() == CharacterType.Werewolf) { // Wolf

                    if (gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)).getCharacterType() != CharacterType.Werewolf) {
                        votings.computeIfPresent(playerPrefixmap.get(playerPrefix), (aLong, integer) -> (integer = integer + 1));
                        alreadyVoted.add(voter);
                        LOGGER.info(currVoter.getUsername() + " hat für " + gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)).getUsername() + " gestimmt");
                    }

                } else if (currVoter.getCharacterType() == CharacterType.Seer) { // Seher
                    // todo embed message
                    currVoter.sendMessage(gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)).getCharacterType().toString() + " lol");
                    System.out.println("works?");
                    alreadyVoted.add(voter);
                    LOGGER.info(currVoter.getUsername() + " schaut " + gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)).getUsername() + "s Rolle an");
                }
            }


            //Check ob jeder Werewolf (und Seher) gevotet hat/-ben
            for (Player player : gameController.getGame().getPlayers()) {
                if (player.isAlive() && (player.getCharacterType() == CharacterType.Werewolf || player.getCharacterType() ==  CharacterType.Seer)) {
                    if (!alreadyVoted.contains(player.getId())) {
                        finished = false;
                    }
                }
            }


        } else {
            if (currVoter.isAlive()) {
                votings.computeIfPresent(playerPrefixmap.get(playerPrefix), (aLong, integer) -> (integer = integer + 1));
                alreadyVoted.add(voter);
                LOGGER.info(currVoter.getUsername() + " hat für " + gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)).getUsername() + " gestimmt");
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
                loggingSB.append(gameController.getGame().getPlayer(entry.getKey()).getUsername() + " hat " + entry.getValue() + "Stimmen\r");
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