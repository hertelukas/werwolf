package com.werwolf.helpers;

import com.werwolf.game.Game;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONReader {
    private final static Logger LOGGER = LoggerFactory.getLogger(JSONReader.class);

    JSONObject document;

    JSONObject parseFile(String fileLocation){
        JSONObject result;
        try {
            File file = new File(new URI("src/main/resources/NightStories.json").toString());
            result = new JSONObject(FileUtils.readFileToString(file, "utf-8"));
            return result;
        }
        catch (Exception e){
            LOGGER.warn("Failed to read JSON: " + e.getMessage());
            return null;
        }
    }

    public String getStory(Game game, int index){
        JSONArray array = document.getJSONArray("stories");
        int rnd = new Random().nextInt((array.length()));
        JSONObject randomStory = array.getJSONObject(rnd);

        if(game.getTumMode()){
            //Todo Handle language
            return randomStory.getString("englishTUM");
        }
        else{
            return randomStory.getString("english");
        }
    }
}
