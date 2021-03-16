package com.werwolf.core.handler.message.configs;

import org.springframework.stereotype.Service;

@Service
public class ConfigSeer extends Config {

    public ConfigSeer() {
        setName("Amount of Seer");
        setCommand("seer");
        setDescription("Sets the amount of Seers");
    }
}
