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

    public boolean die(Game game) {
        if (!isSavedByBodyguard()) {
            this.isAlive = false;
            if (game.getController().isNight())
                noScope(game);
            if (game.getTumMode()) sendMessage("https://bit.ly/unexzellent");
            sendMessage("You died, the dude you voted for hopefully died too lol");
            if (game.getConfigurations().isShowRole()) {
                EmbedBuilder showRole = new EmbedBuilder();
                showRole.setTitle(getUsername()).setDescription(getUsername() + UserMessageCreator.getCreator().getMessage(game, "death-Message") + characterType);
                game.getChannel().sendMessage(showRole.build()).queue();
            }
            return true;
        } else
            return false;
    }

    @Override
    public void saveVote(Player votedGuy) { // fix?
       // System.out.println("Hunter vote bueno?: " + (votedGuy != null));
        votedPlayer = votedGuy;
    }

    @Override
    public void noScope(Game game) {
        game.getController().getNightController().getNights().peek().getDiedtonight().add(votedPlayer);
        votedPlayer.die(game);
    }
}
