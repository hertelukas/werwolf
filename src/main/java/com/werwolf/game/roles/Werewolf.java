package com.werwolf.game.roles;

import com.werwolf.core.handler.AudioHandler;
import com.werwolf.game.Game;
import com.werwolf.game.Night;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Werewolf extends Player {


    private final static Logger LOGGER = LoggerFactory.getLogger(Werewolf.class);
    private boolean hasVoted=false;

    public Werewolf(Player player){
        super(player.user, player.guild);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        characterType = CharacterType.Werewolf;
    }

    /**
     * Werewolf stimmt ab
     * @param target Spieler der gevotet wurde
     * @param votings Liste der Stimmen der Werewölfe
     */
    @Override
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
        if (!canVote()) return;

        if (target.getCharacterType() != CharacterType.Werewolf) {
            LOGGER.info(game.getPlayer(target.getId()).getUsername() + " wurde vom Werewolf " + getUsername() + " ausgewählt");
            votings.computeIfPresent(target.getId(), (aLong, integer) -> (integer = integer + 1));
            hasVoted = true;
        }
        boolean lastVoting = true;
        for (Player player : game.getPlayers()) {
            if (player instanceof Werewolf && !((Werewolf) player).hasVoted && player.canVote()) {
                lastVoting = false;
            }
        }

        if (lastVoting) {
            killMostVoted(votings, game);
        }

    }

    private void killMostVoted(HashMap<Long, Integer> votings, Game game) {

        Map.Entry<Long, Integer> votedPlayer = null;
        for (Map.Entry<Long, Integer> player : votings.entrySet()) {
            if (votedPlayer == null) votedPlayer = player;

            if (votedPlayer.getValue() < player.getValue()) votedPlayer = player;
            LOGGER.info(game.getPlayer(player.getKey()).getUsername() + " hat " + player.getValue() + " Stimmen");
        }

        if (votedPlayer.getValue() == 0) {
            votedPlayer = null;
            LOGGER.info("Die Werewölfe bringen niemanden um");
        } else {
            if (game.getPlayer(votedPlayer.getKey()).die(game)) {
                LOGGER.info("Die Werewölfe bringen " + game.getPlayer(votedPlayer.getKey()).getUsername() + " um");
                game.getController().getNightController().getNights().peek().getDiedtonight().add(game.getPlayer(votedPlayer.getKey()));
            } else {
                votedPlayer = null;
            }
        }
    }

    /**
     * Jeder Werwolf hat für nächste Nacht wieder nicht gevotet
     * @param game
     */
    @Override
    public void reset(Game game){
        for (Player player : game.getPlayers()) {
            if (player instanceof Werewolf) {
                ((Werewolf) player).hasVoted = false;
            }
        }
    }

}