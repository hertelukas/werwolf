package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigHunter extends Config {

    public ConfigHunter() {
        setName("Amount of Hunters");
        setCommand("hunter");
        setDescription("Sets the amount of hunters");
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getHunternum());
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        try {
            game.getConfigurations().setHunternum(Integer.parseInt(arg) < 0 ? 0 : Integer.parseInt(arg));
        } catch (NumberFormatException e) {
            game.getChannel().sendMessage("Conifg parameter for hunter must be a number").queue();
        }
        return true;
    }
}