package com.werwolf.game.controller;

import com.werwolf.game.roles.Player;
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
    private Player majorsVote;

    public VotingController(GameController controller) {
        this.gameController = controller;
    }

    public void newVoting(boolean isNight) {
        votings = new HashMap<>();
        playerPrefixmap = new HashMap<>();
        this.nightVoting = isNight;
        alreadyVoted = new ArrayList<>();
        savedReaction = new HashMap<>();
        majorsVote = null;
    }

    public void addPlayer(String playerPrefix, long playerID) {
        playerPrefixmap.put(playerPrefix, playerID);
        votings.put(playerID, 0);
    }

    public void vote(String playerPrefix, long voter) {

        boolean finished = true;
        Player currVoter = gameController.getGame().getPlayer(voter); // der Dude, der gevotet hat

        //This might happen when the voter is not a player
        if(currVoter == null)return;

        Player votedPlayer;

        //VOTING IN DER NACHT
        if (nightVoting) {

            votedPlayer = gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)); // der Dude, der gevotet wurde
            if(votedPlayer == null){
                LOGGER.warn(currVoter.getUsername() + " voted for skip in the night. This shouldn't be possible");
                return;
            }

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
                alreadyVoted.sort(Comparator.comparingInt(p -> p.getCharacterType().getPriority()));
                for (Player p : alreadyVoted) {
                    p.vote(savedReaction.get(p), votings, gameController.game);
                }
            }



        //VOTING AM TAG
        } else {
            if(playerPrefixmap.get(playerPrefix) == -1) votedPlayer = null;
            else votedPlayer = gameController.getGame().getPlayer(playerPrefixmap.get(playerPrefix)); // der Dude, der gevotet wurde

            if (currVoter.isAlive()) {
                votings.computeIfPresent(playerPrefixmap.get(playerPrefix), (aLong, integer) -> (integer = integer + 1));
                if (!alreadyVoted.contains(currVoter)) {
                    alreadyVoted.add(currVoter);
                    if(votedPlayer == null) {
                        currVoter.sendMessage(UserMessageCreator.getCreator().getMessage(gameController.getGame(), "skip-vote"));
                        LOGGER.info(currVoter.getUsername() + " hat für nen Skip gevoted");
                        if (currVoter.isMajor()) majorsVote = null;
                    }
                    else{
                        currVoter.sendMessage(UserMessageCreator.getCreator().getMessage(gameController.getGame(), "voted-for") + votedPlayer.getUsername());
                        LOGGER.info(currVoter.getUsername() + " hat für " + votedPlayer.getUsername() + " gestimmt");
                        if (currVoter.isMajor()) majorsVote = votedPlayer;
                    }
                }
            }

            //Check if a player still has to vote
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
                if(entry.getKey() == -1) continue;
                loggingSB.append(gameController.getGame().getPlayer(entry.getKey()).getUsername())
                        .append(" hat ")
                        .append(entry.getValue())
                        .append("Stimmen\r");
            }
            LOGGER.info(loggingSB.toString());
            gameController.waitForHunter();
            gameController.setVoting(false);
            gameController.continueAfterVoting();
        }
    }


    public HashMap<Long, Integer> getResult() {
        List<Long> sameAmount = new ArrayList<>();
        int max = 0;

        for (Map.Entry<Long, Integer> entry : votings.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                sameAmount = new ArrayList<>();
                sameAmount.add(entry.getKey());
            } else if (entry.getValue() == max) {
                sameAmount.add(entry.getKey());
            }
        }

        if (sameAmount.size() <= 1) {
            LOGGER.info(votings.toString());
        } else {
            EmbedBuilder majorVote = new EmbedBuilder();
            majorVote.setTitle(UserMessageCreator.getCreator().getMessage(gameController.game, "mayor-votetitle"));
            gameController.getGame().getChannel().sendMessage(majorVote.build()).queue();
            votings.computeIfPresent(majorsVote.getId(), (mv, val) -> val = 10000);
        }
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



        if(voter.getCharacterType().canVote()) {
            voter.sendMessage("Vote received!");
            LOGGER.info(voter.getUsername() + "(" + gameController.game.getPlayer(voter.getId()).getCharacterType() + ") auf " + target.getUsername() + " (" + gameController.game.getPlayer(target.getId()).getCharacterType() + ") zwischengespeichert");
            savedReaction.put(voter, target);
        }

        alreadyVoted.add(voter);
    }
}