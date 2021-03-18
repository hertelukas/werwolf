package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigWrite extends Config {

    public ConfigWrite() {
        setName("Write Permission");
        setCommand("write");
        setDescription("Enables or disables write permission in the main channel (on/off)");
    }

    @Override
    public String getConfigResult(Game game) {
        return game.getConfigurations().canWrite() ? "on" : "off";
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        game.getConfigurations().setWrite(arg.equals("on"));
        game.setMainWritePermissions();

        return true;
    }
}
