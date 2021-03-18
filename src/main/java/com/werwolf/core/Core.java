package com.werwolf.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

@Configuration
public class Core {

    @Value("${token}")
   // @Value("ODE5NjQ4ODYyMjg5MjY0NzUw.YEprfQ.HVglR4A3M1CmGVYLc53R5QgsfaA")
    private String token;

    @Bean
    public JDA login(MainListener mainListener) throws LoginException {
        EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGES
        );

        return JDABuilder.create(token, intents).addEventListeners(mainListener).build();
    }


}
