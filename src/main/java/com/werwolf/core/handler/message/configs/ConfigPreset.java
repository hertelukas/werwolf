package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigPreset extends Config {

    public ConfigPreset() {
        setName("Preset");
        setCommand("preset");
        setDescription("Sets the preset");
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getPreset());
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        try {
            game.getConfigurations().setPreset(Math.max(Integer.parseInt(arg), 0));
        } catch (NumberFormatException e) {
            game.getChannel().sendMessage("Config number pls").queue();
        }
        return true;
    }
}
