package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigSeer extends Config {

    public ConfigSeer() {
        setName("Amount of seers");
        setCommand("seer");
        setDescription("Sets the amount of seers");
        setRole(true);
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getSeernum());
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        if(arg == null){
            game.getConfigurations().setSeernum(1);
            return true;
        }

        try {
            game.getConfigurations().setSeernum(Math.max(Integer.parseInt(arg), 0));
        } catch (NumberFormatException e) {
            game.getChannel().sendMessage("Config parameter for seer must be a number").queue();
        }
        return true;
    }
}
