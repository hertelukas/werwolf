package com.werwolf.game.roles;

import com.werwolf.game.Game;
import com.werwolf.helpers.UserMessageCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Random;

public class Sheriff extends Villager {

    private final static Logger LOGGER = LoggerFactory.getLogger(Sheriff.class);
    private static final int SHERIFF_AVG_SUCCESS = 20;

    public Sheriff(Player player) {
        super(player);
        this.username = player.username;
        this.id = player.id;
        this.isAlive = player.isAlive;
        this.user = player.user;
        setCharacterType(CharacterType.Sheriff);
    }

    /**
     *
     * @param target Spieler, von dem der Sheriff mehr Informationen bekommt
     * @param votings Hashmap, die die Voting der Werewölfe beinhaltet (Falls später auch noch andere darauf zugriff haben sollen)
     */
    @Override
    public void vote(Player target, HashMap<Long, Integer> votings, Game game) {
        Random rd = new Random();
        int chanceForTrueInformation = rd.nextInt(SHERIFF_AVG_SUCCESS) + 100 - SHERIFF_AVG_SUCCESS;
        int success = rd.nextInt(100);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(UserMessageCreator.getCreator().getMessage(game, "sheriff-report"));
        builder.addField(UserMessageCreator.getCreator().getMessage(game, "certainty"), chanceForTrueInformation + ((100 - chanceForTrueInformation) / 2) + "%", false );
        //If we have success we send true information
        if(success < chanceForTrueInformation){
            //Todo alle Rollen auflisten die in der Nacht nichts machen
            if(target.getCharacterType() != CharacterType.Werewolf)
                builder.setDescription(target.getUsername() + UserMessageCreator.getCreator().getMessage(game,  "sheriff-report-home"));
            else
                builder.setDescription(target.getUsername() + UserMessageCreator.getCreator().getMessage(game, "sheriff-report-sus"));
        }else{
            //This information is random
            if(rd.nextBoolean())
                builder.setDescription(target.getUsername() + UserMessageCreator.getCreator().getMessage(game, "sheriff-report-home"));
            else
                builder.setDescription(target.getUsername() + UserMessageCreator.getCreator().getMessage(game, "sheriff-report-sus"));
        }
        sendMessage(builder.build());
        LOGGER.info(getUsername() + " untersucht " + target.getUsername());
    }
}
