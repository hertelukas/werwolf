package com.werwolf.game;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URI;
import java.util.List;


public class Night {

    private static final int STORY_AMOUNT = 2;

    private List<Player> alive;

    private String story = getRandomStory();

    public Night(List<Player> alive) {
        this.alive = alive;
    }

    private String getRandomStory() {
        String story = "ERROR";
        try {
            File file = new File(new URI("src/main/resources/Stories.xml").toString());
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            int randy = (int) (Math.random() * STORY_AMOUNT);
            story = document.getElementsByTagName("story" + randy).item(0).getTextContent();
        } catch (Exception e) {
            return "ERROR";
        }
        return story;
    }

    public List<Player> getAlive() {
        return alive;
    }

    public String getStory() {
        return story;
    }
}
