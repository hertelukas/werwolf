package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigProstitute extends Config {

    public ConfigProstitute() {
        setName("Amount of prostitutes");
        setCommand("prostitute");
        setDescription("Sets the amount of prostitutes");
        setRole(true);
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getPrositutesnum());
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        if (arg == null) {
            game.getConfigurations().setPrositutesnum(1);
            return true;
        }

        try {
            game.getConfigurations().setPrositutesnum(Math.max(Integer.parseInt(arg), 0));
        } catch (NumberFormatException e) {
            game.getChannel().sendMessage("Config parameter for bodyguard must be a number").queue();
        }

        return true;
    }
}
