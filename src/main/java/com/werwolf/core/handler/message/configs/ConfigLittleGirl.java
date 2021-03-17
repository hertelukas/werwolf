package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigLittleGirl extends Config {

    public ConfigLittleGirl() {
        setName("Amount of little girls");
        setCommand("littlegirl");
        setDescription("Sets the amount of little girls");
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getLittleGirlnum());
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        try {
            game.getConfigurations().setLittleGirlnum(Math.max(Integer.parseInt(arg), 0));
        } catch (NumberFormatException e) {
            game.getChannel().sendMessage("Config parameter for little girl must be a number").queue();
        }
        return true;
    }
}
