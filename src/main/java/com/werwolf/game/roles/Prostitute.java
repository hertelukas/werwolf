package com.werwolf.game.roles;

import com.werwolf.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Prostitute extends Villager {

    private final static Logger LOGGER = LoggerFactory.getLogger(Prostitute.class);

    private Player visitedPlayer = null;

    public Prostitute(Player player) {
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        setCharacterType(CharacterType.Prostitute);
    }

    @Override
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
        if (!canVote()) return;

        LOGGER.info(game.getPlayer(target.getId()).getUsername() + " wird von der Nutte " + getUsername() + " besucht");
        target.setHasSex(true);
        visitedPlayer = target;
        target.whore = this;
    }

    @Override
    public boolean die(Game game) {
        if (visitedPlayer == null || visitedPlayer == this) {
            LOGGER.info("Nutte wird bei sich zu Hause umgebracht");
            return super.die(game);
        } else{
            LOGGER.info("Nutte ist nicht zu hause und wird folglich nicht umgebracht");
            return false;
        }
    }

    public boolean dieSure(Game game) {
        return super.die(game);
    }

    /**
     * entschützt den gesicherten Spieler
     * @param game
     */
    @Override
    public void reset(Game game) {
        if (visitedPlayer == null) return;
        visitedPlayer.setHasSex(false);
        visitedPlayer.whore = null;
        visitedPlayer = null;

    }
}
