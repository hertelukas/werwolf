package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigAudio extends Config {

    public ConfigAudio() {
        setName("Enable Audio");
        setCommand("audio");
        setDescription("Enables or disables audio (on/off)");
    }

    @Override
    public String getConfigResult(Game game) {
        return game.getConfigurations().playAudio() ? "on" : "off";
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        game.getConfigurations().setPlayAudio(arg.equals("on"));

        return true;
    }
}
