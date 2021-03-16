package com.werwolf.helpers;

import com.werwolf.game.Game;
import org.json.JSONArray;
import org.json.JSONObject;

public class IntroTextCreator extends JSONReader{

    static IntroTextCreator creator = new IntroTextCreator();

    private IntroTextCreator() {
        document = parseFile("src/main/resources/IntroStories.json");
    }

    public static IntroTextCreator getCreator(){
        return creator;
    }
}
