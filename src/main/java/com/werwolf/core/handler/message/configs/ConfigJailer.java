package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigJailer extends Config {
    public ConfigJailer() {
        setName("Amount of jailers");
        setCommand("jailer");
        setDescription("Sets the amount of jailers");
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getJailornum());
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        try {
            game.getConfigurations().setJailornum(Math.max(Integer.parseInt(arg), 0));
        } catch (NumberFormatException e) {
            game.getChannel().sendMessage("Config parameter for jailer must be a number").queue();
        }
        return true;
    }
}
