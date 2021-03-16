package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;

public abstract class Config {

    private String name;
    private String description;
    private String command;

    public Config() {
    }

    public void setCommand(String command){
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public abstract String getConfigResult(Game game);
}
