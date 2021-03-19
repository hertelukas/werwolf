package com.werwolf.core.handler.message.configs;

import com.werwolf.game.Game;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Service;

@Service
public class ConfigPreset extends Config {

    public ConfigPreset() {
        setName("Preset");
        setCommand("preset");
        setDescription("Sets the preset. You can check available presets with `config preset list`");
    }

    @Override
    public String getConfigResult(Game game) {
        return Integer.toString(game.getConfigurations().getPreset());
    }

    @Override
    public boolean updateConfig(Game game, String command, String arg) {
        if (!command.equals(getCommand()) || game == null) return false;

        //List all presets
        if(arg != null && arg.equals("list")){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Role Presets");
            builder.addField("Preset 1", "Every role is set to 1", false);
            builder.addField("Preset 2", "Normal \nWitches: 1\nSeer: 1\nHunter: 1\nGirl: 1", false);
            builder.addField("Preset 3", "Strategy \nWitches: 1\nSeer: 0\nSheriff: 1\nJailor: 2\nBodyguard: 2\nKiller: on", false);

            game.getChannel().sendMessage(builder.build()).queue();

            return true;
        }

        try {
            game.getConfigurations().setPreset(Math.max(Integer.parseInt(arg), 0));
            game.presetCheck(game.getConfigurations().getPreset());
        } catch (NumberFormatException e) {
            game.getChannel().sendMessage("Config number pls").queue();
        }
        return true;
    }
}
