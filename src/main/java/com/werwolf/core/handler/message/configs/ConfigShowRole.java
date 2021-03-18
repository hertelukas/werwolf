package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigShowRole extends Config {

    public ConfigShowRole() {
        setName("Showrole");
        setCommand("showrole");
        setDescription("Show the role of people who died");
    }

    @Override
    public String getConfigResult(Game game) {
        return game.getConfigurations().isShowRole() ? "on" : "off";
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        game.getConfigurations().setShowRole(arg.equals("on"));
        return true;
    }
}