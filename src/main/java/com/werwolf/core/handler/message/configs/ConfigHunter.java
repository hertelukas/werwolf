package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigHunter extends Config {

    public ConfigHunter() {
        setName("Amount of Hunters");
        setCommand("hunter");
        setDescription("Sets the amount of hunters");
        setRole(true);
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getHunternum());
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        if(arg == null){
            game.getConfigurations().setHunternum(1);
            return true;
        }

        try {
            game.getConfigurations().setHunternum(Math.max(Integer.parseInt(arg), 0));
        } catch (NumberFormatException e) {
            game.getChannel().sendMessage("Config parameter for hunter must be a number").queue();
        }
        return true;
    }
}
