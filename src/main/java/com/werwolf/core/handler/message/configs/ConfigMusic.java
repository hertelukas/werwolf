package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigMusic extends Config {

    public ConfigMusic() {
        setName("Enable Music");
        setCommand("audio");
        setDescription("Enables or disables audio (on/off)");
    }

    @Override
    public String getConfigResult(Game game) {
        return game.getConfigurations().playMusic() ? "on" : "off";
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        game.getConfigurations().setPlayMusic(arg.equals("on"));

        return true;
    }
}
