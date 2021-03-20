package com.werwolf.helpers;

public class DayTextCreator extends JSONReader{

    static DayTextCreator creator = new DayTextCreator();

    private DayTextCreator() {
        document = parseFile("src/main/data/DayStories.json");
    }

    public static DayTextCreator getCreator(){
        return creator;
    }
}
