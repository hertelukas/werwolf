package com.werwolf.game.roles;

import com.werwolf.game.Game;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;


public class Hunter extends Villager {

    Player votedPlayer = null;

    public Hunter(Player player) {
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
            // sendMessage("You died, the dude you voted for hopefully died too lol");
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
            if (votedPlayer.die(game))
                game.getController().getNightController().getNights().peek().getDiedtonight().add(votedPlayer);
        }
//            else
//                sendMessage("Could not no scope :(");
//        } else {
//            sendMessage("Could not no scope :(");
//        }
    }
}
