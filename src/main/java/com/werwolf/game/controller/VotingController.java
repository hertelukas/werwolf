package com.werwolf.game.controller;

import com.werwolf.game.CharacterType;
import com.werwolf.game.Player;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class VotingController {

    private final static Logger LOGGER = LoggerFactory.getLogger(VotingController.class);
    private static final int SHERIFF_AVG_SUCCESS = 40;

    private final GameController gameController;
    private HashMap<Long, Integer> votings = new HashMap<>();
    private HashMap<String, Long> playerPrefixmap = new HashMap<>();
    //Player a greift -> Player b an
    private HashMap<Player, Player> savedReaction = new HashMap<>();
    private boolean nightVoting = false;
    private List<Player> alreadyVoted = new ArrayList<>();

    public VotingController(GameController controller) {
        this.gameController = controller;
    }

    public void newVoting(boolean isNight) {
        votings = new HashMap<>();
        playerPrefixmap = new HashMap<>();
        this.nightVoting = isNight;
        alreadyVoted = new ArrayList<>();
        savedReaction = new HashMap<>();
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
                if (!alreadyVoted.contains(currVoter)) {
                    computeVote(currVoter, votedPlayer);
                }
            }


            //Check ob jeder der Voten kann gevotet hat/-ben
            for (Player player : gameController.getGame().getPlayers()) {
                if (player.isAlive() && player.canVote()) {
                    if (!alreadyVoted.contains(player)) {
                        finished = false;
                    }
                }
            }

            //Votes werden ausgeführt wenn die Person voten darf
            if (finished) {
                for (Player p : alreadyVoted) {
                    if (p.canVote()) {
                        switch (p.getCharacterType()) {
                            case Werewolf -> voteAsWerewolf(savedReaction.get(p), p, playerPrefix);
                            case Seer -> voteAsSeer(savedReaction.get(p), p, playerPrefix);
                            case Sheriff -> voteAsSheriff(savedReaction.get(p), p, playerPrefix);
                            case Bodyguard -> voteAsBodyguard(savedReaction.get(p), p, playerPrefix);
                        }
                    } else if (p.isJailed()) {
                        p.sendMessage(UserMessageCreator.getCreator().getMessage(gameController.game, "jailor-jails"));
                        p.setJailed(false);
                    }
                }
            }


        } else {
            if (currVoter.isAlive()) {
                votings.computeIfPresent(playerPrefixmap.get(playerPrefix), (aLong, integer) -> (integer = integer + 1));
                if (!alreadyVoted.contains(currVoter)) {
                    alreadyVoted.add(currVoter);
                    currVoter.sendMessage("Du hast für " + votedPlayer.getUsername() + "gestimmt");
                    LOGGER.info(currVoter.getUsername() + " hat für " + votedPlayer.getUsername() + " gestimmt");
                }
            }

            for (Player player : gameController.getGame().getPlayers()) {
                if (player.isAlive()) {
                    if (!alreadyVoted.contains(player)) {
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
     * Bekommt die ganzen Votes und speichert sie in einer Liste zwischen, gibt eine Nachricht an den Spieler aus
     * @param voter Spieler der die Stimme abgegeben hat
     * @param target Spieler der gevotet wurde
     */
    private void computeVote(Player voter, Player target) {
        if(voter.getId() == target.getId()){
            voter.sendMessage(UserMessageCreator.getCreator().getMessage(gameController.getGame(), "self-vote"));
            return;
        }
        if (voter.getCharacterType() == CharacterType.Jailor) {
            LOGGER.info(voter.getUsername() + " hat " + target.getUsername() + "(" + gameController.game.getPlayer(target.getId()).getCharacterType() + ") erflogreich gejailt");
            target.setJailed(true);
        } else {
            LOGGER.info(voter.getUsername() + "(" + gameController.game.getPlayer(voter.getId()).getCharacterType() + ") auf " + target.getUsername() + " (" + gameController.game.getPlayer(target.getId()).getCharacterType() + ") zwischengespeichert");
            savedReaction.put(voter, target);
        }
        voter.sendMessage("Vote received!");
        alreadyVoted.add(voter);
    }

    /**
     * Werewolf stimmt ab
     *
     * @param target Spieler der gevotet wurde
     * @param voter Spieler der die Stimme abgegeben hat
     * @param playerPrefix
     */
    private void voteAsWerewolf(Player target, Player voter, String playerPrefix) {
        if (target.getCharacterType() != CharacterType.Werewolf) {
            votings.computeIfPresent(playerPrefixmap.get(playerPrefix), (aLong, integer) -> (integer = integer + 1));
            LOGGER.info(voter.getUsername() + " hat für " + target.getUsername() + " gestimmt");
        }
    }

    private void voteAsBodyguard(Player votedPlayer, Player currVoter, String playerPrefix) {

        votedPlayer.setSavedByBodyguyard(true);

    }


    /**
     * Seher stimmt ab
     * @param target Spieler der gevotet wurde
     * @param voter Spieler der die Stimme abgegeben hat
     * @param playerPrefix
     */
    private void voteAsSeer(Player target, Player voter, String playerPrefix) {
        // todo embed message oder grundsätzlich etwas verschönern
        voter.sendMessage(target.getUsername() + ": " + target.getCharacterType());
        LOGGER.info(voter.getUsername() + " schaut " + target.getUsername() + "s Rolle an");

    }

    /**
     * Sheriff stimmt ab
     * @param votedPlayer
     * @param voter
     * @param playerPrefix
     */
    private void voteAsSheriff(Player votedPlayer, Player voter, String playerPrefix) {
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
        voter.sendMessage(builder.build());
        LOGGER.info(voter.getUsername() + " untersucht " + votedPlayer.getUsername());
    }
}