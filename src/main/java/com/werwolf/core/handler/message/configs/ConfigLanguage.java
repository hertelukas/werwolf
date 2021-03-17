package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConfigLanguage extends Config{

    public ConfigLanguage(){
        setName("Set the language of the game");
        setCommand("language");
        setDescription("Choose if the game should be in english (en) or in german (de)");

    }

    @Override
    public String getConfigResult(Game game) {
        return game.getConfigurations().isEnglish() ? "English" : "German";
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if(!command.equals(getCommand()) || game == null) return false;

        game.getConfigurations().setEnglish(arg.equals("en"));

        return true;
    }
}
