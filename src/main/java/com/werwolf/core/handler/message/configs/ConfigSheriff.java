package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigSheriff extends Config {

    public ConfigSheriff() {
        setName("Amount of Sheriffs");
        setCommand("sheriff");
        setDescription("Sets the amount of sheriffs");
        setRole(true);
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getSheriffnum());
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        try {
            game.getConfigurations().setSheriffnum(Math.max(Integer.parseInt(arg), 0));
        } catch (NumberFormatException e) {
            game.getChannel().sendMessage("Config parameter for sheriff must be a number").queue();
        }
        return true;
    }
}