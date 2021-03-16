package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigSeer extends Config {

    public ConfigSeer() {
        setName("Amount of seers");
        setCommand("seer");
        setDescription("Sets the amount of seers");
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getSeernum());
    }
}
