package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigSpy extends Config {

    public ConfigSpy() {
        setName("Amount of Spies");
        setCommand("spy");
        setDescription("Sets the amount of spies");
        setRole(true);
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getSpynum());
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        if (arg == null) {
            game.getConfigurations().setSpynum(1);
            return true;
        }

        try {
            game.getConfigurations().setSpynum(Math.max(Integer.parseInt(arg), 0));
        } catch (NumberFormatException e) {
            game.getChannel().sendMessage("Config parameter for spies must be a number").queue();
        }

        return true;
    }
}
