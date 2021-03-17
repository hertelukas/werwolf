package com.werwolf.game.controller;

import com.werwolf.game.CharacterType;
import com.werwolf.game.Player;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.*;

public class VotingController {

    private final static Logger LOGGER = LoggerFactory.getLogger(VotingController.class);

    private final GameController gameController;
    private HashMap<Long, Integer> votings = new HashMap<>();
    private HashMap<String, Long> playerPrefixmap = new HashMap<>();
    private boolean nightVoting = false;
    private List<Long> alreadyVoted = new ArrayList<>();
    private static final int SHERIFF_AVG_SUCCESS = 40;

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
        Player votedPlayer = gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)); // der Dude, der gevotet wurde

        if (nightVoting) {
            if (currVoter.isAlive()) {


                //Wenn der Spieler noch nicht gevotet hat wird sein vote akzeptiert und passend verarbeitet
                if (!alreadyVoted.contains(voter) && currVoter.canVote()) {
                    switch (currVoter.getCharacterType()) {
                        case Werewolf -> voteAsWerewolf(votedPlayer, currVoter, playerPrefix);
                        case Seer -> voteAsSeer(votedPlayer, currVoter, playerPrefix);
                        case Sheriff -> voteAsSheriff(votedPlayer, currVoter, playerPrefix);
                    }
                    alreadyVoted.add(voter);
                }
            }


            //Check ob jeder der Voten kann gevotet hat/-ben
            for (Player player : gameController.getGame().getPlayers()) {
                if (player.isAlive() && player.canVote()) {
                    if (!alreadyVoted.contains(player.getId())) {
                        finished = false;
                    }
                }
            }


        } else {
            if (currVoter.isAlive()) {
                votings.computeIfPresent(playerPrefixmap.get(playerPrefix), (aLong, integer) -> (integer = integer + 1));
                alreadyVoted.add(voter);
                LOGGER.info(currVoter.getUsername() + " hat für " + votedPlayer.getUsername() + " gestimmt");
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

    /**
     * Werewolf stimmt ab
     *
     * @param votedPlayer
     * @param currVoter
     * @param playerPrefix
     */
    private void voteAsWerewolf(Player votedPlayer, Player currVoter, String playerPrefix) {
        if (votedPlayer.getCharacterType() != CharacterType.Werewolf) {
            votings.computeIfPresent(playerPrefixmap.get(playerPrefix), (aLong, integer) -> (integer = integer + 1));
            LOGGER.info(currVoter.getUsername() + " hat für " + votedPlayer.getUsername() + " gestimmt");
        }
    }

    /**
     * Seher stimmt ab
     * @param votedPlayer
     * @param currVoter
     * @param playerPrefix
     */
    private void voteAsSeer(Player votedPlayer, Player currVoter, String playerPrefix) {
        // todo embed message oder grundsätzlich etwas verschönern
        currVoter.sendMessage(votedPlayer.getUsername() + ": " + votedPlayer.getCharacterType());
        LOGGER.info(currVoter.getUsername() + " schaut " + votedPlayer.getUsername() + "s Rolle an");

    }

    /**
     * Sheriff stimmt ab
     * @param votedPlayer
     * @param currVoter
     * @param playerPrefix
     */
    private void voteAsSheriff(Player votedPlayer, Player currVoter, String playerPrefix) {
        Random rd = new Random();
        int chanceForTrueInformation = rd.nextInt(SHERIFF_AVG_SUCCESS) + 100 - SHERIFF_AVG_SUCCESS;
        int success = rd.nextInt(100);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(UserMessageCreator.getCreator().getMessage(gameController.getGame(), "sheriff-report"));
        builder.addField(UserMessageCreator.getCreator().getMessage(gameController.getGame(), "certainty"), chanceForTrueInformation + ((100 - chanceForTrueInformation) / 2) + "%", false );
        //If we have success we send true information
        if(success < chanceForTrueInformation){
            //Todo alle Rollen auflisten die in der Nacht nichts machen
            if(votedPlayer.getCharacterType() == CharacterType.Villager)
                builder.setDescription(votedPlayer.getUsername() + UserMessageCreator.getCreator().getMessage(gameController.getGame(),  "sheriff-report-home"));
            else
                builder.setDescription(votedPlayer.getUsername() + UserMessageCreator.getCreator().getMessage(gameController.getGame(), "sheriff-report-sus"));
        }else{
            //This information is random
            if(rd.nextBoolean())
                builder.setDescription(votedPlayer.getUsername() + UserMessageCreator.getCreator().getMessage(gameController.getGame(), "sheriff-report-home"));
            else
                builder.setDescription(votedPlayer.getUsername() + UserMessageCreator.getCreator().getMessage(gameController.getGame(), "sheriff-report-sus"));
        }
        currVoter.sendMessage(builder.build());
        LOGGER.info(currVoter.getUsername() + " untersucht " + votedPlayer.getUsername());
    }
}