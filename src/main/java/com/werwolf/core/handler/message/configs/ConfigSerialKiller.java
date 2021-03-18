package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigSerialKiller extends Config{

    public ConfigSerialKiller(){
        setName("Serial Killer");
        setCommand("killer");
        setDescription("Enables or disables serial killer (on/off)");
        setRole(true);
    }

    @Override
    public String getConfigResult(Game game) {
        return game.getConfigurations().getSerialkiller() ? "on" : "off";
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        game.getConfigurations().setSerialkiller(arg.equals("on"));

        return true;
    }
}
