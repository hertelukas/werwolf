package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import org.springframework.stereotype.Service;

@Service
public class ConifWitch extends Config{

    public ConifWitch() {
        setName("Amount of witches");
        setCommand("witch");
        setDescription("Sets the amount of witches");
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getWitchnum());
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        try {
            game.getConfigurations().setWitchnum(Integer.parseInt(arg) < 0 ? 0 : Integer.parseInt(arg));
        } catch (NumberFormatException e) {
            game.getChannel().sendMessage("Conifg parameter for witch must be a number").queue();
        }
        return true;
    }
}
