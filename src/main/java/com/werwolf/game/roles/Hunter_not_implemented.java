package com.werwolf.game.roles;

import com.werwolf.game.Game;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;

public class Hunter_not_implemented extends Villager {

    public Hunter_not_implemented(Player player){
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        characterType = CharacterType.Hunter;
    }

    public void die(Game game) {
        this.isAlive = false;

        sendMessage("You died.");

        // hunter soll dann voten

        if (game.getConfigurations().isShowRole()) {
            EmbedBuilder showRole = new EmbedBuilder();
            showRole.setTitle(getUsername()).setDescription(getUsername() + UserMessageCreator.getCreator().getMessage(game, "death-Message") + characterType);

            game.getChannel().sendMessage(showRole.build()).queue();
        }
    }
}
