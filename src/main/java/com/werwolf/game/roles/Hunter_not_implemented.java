package com.werwolf.game.roles;

import com.werwolf.game.Game;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;


public class Hunter_not_implemented extends Villager {

    boolean voteValid = false;
    Player votedPlayer = null;

    public Hunter_not_implemented(Player player) {
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        characterType = CharacterType.Hunter;
    }

    @Override
    public boolean die(Game game) {
        if (super.die(game)) {
            if (game.getController().isNight())
                noScope(game);
            sendMessage("You died, the dude you voted for hopefully died too lol");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void saveVote(Player votedGuy) { // fix?
        // System.out.println("Hunter vote bueno?: " + (votedGuy != null));
        votedPlayer = votedGuy;
    }

    @Override
    public void noScope(Game game) {
        if (votedPlayer != null) {
            game.getController().getNightController().getNights().peek().getDiedtonight().add(votedPlayer);
            votedPlayer.die(game);
        } else {
            sendMessage("Could not no scope :(");
        }
    }
}
