package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;


public abstract class Config {

    private String name;
    private String description;
    private String command;
    private boolean isRole = false;

    public Config() {
    }

    public abstract String getConfigResult(Game game);

    public abstract boolean updateConfig(Game game, String command, String arg);

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

    public boolean isRole() {
        return isRole;
    }

    public void setRole(boolean role) {
        isRole = role;
    }
}
