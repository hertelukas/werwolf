package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;

public class ConfigBodyguard extends Config{

    public ConfigBodyguard(){
        setName("Amount of Bodyguards");
        setCommand("bodyguard");
        setDescription("Sets the amount of bodyguards");
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getBodyguardnum());
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        try{
            game.getConfigurations().setBodyguardnum(Math.max(Integer.parseInt(arg), 0));
        } catch (NumberFormatException e){
            game.getChannel().sendMessage("Config parameter for bodyguard must be a number").queue();
        }

        return true;
    }
}
