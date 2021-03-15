package com.werwolf.game;

import com.werwolf.game.Controler.GameController;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URI;
import java.util.List;


public class Night {

    private List<Player> alive;

    private String story = "";

    private final boolean tumMode;

    public Night(List<Player> alive,boolean tumMode) {
        this.alive = alive;
        this.tumMode = tumMode;
        story = GameController.getRandomStory(tumMode,true);
    }

    public List<Player> getAlive() {
        return alive;
    }

    public String getStory() {
        return story;
    }
}
