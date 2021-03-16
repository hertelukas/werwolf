package com.werwolf.helpers;

import com.werwolf.game.Game;
import org.json.JSONArray;
import org.json.JSONObject;

public class DayTextCreator extends JSONReader{

    static DayTextCreator creator = new DayTextCreator();

    private DayTextCreator() {
        document = parseFile("src/main/resources/DayStories.json");
    }

    public static DayTextCreator getCreator(){
        return creator;
    }
}
