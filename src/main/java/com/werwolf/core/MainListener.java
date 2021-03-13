package com.werwolf.core;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MainListener extends ListenerAdapter {

    @Value("prefix")
    private String prefix;

    public MainListener() {

    }
}
