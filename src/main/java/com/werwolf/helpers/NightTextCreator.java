package com.werwolf.helpers;

import com.werwolf.game.Game;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class NightTextCreator extends JSONReader {

    static NightTextCreator creator = new NightTextCreator();

    private NightTextCreator() {
        document = parseFile("src/main/resources/NightStories.json");
    }



    public static NightTextCreator getCreator() {
        return creator;
    }
}
