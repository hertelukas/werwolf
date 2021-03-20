package com.werwolf.helpers;

public class NightTextCreator extends JSONReader {

    static NightTextCreator creator = new NightTextCreator();

    private NightTextCreator() {
        document = parseFile("src/main/data/NightStories.json");
    }



    public static NightTextCreator getCreator() {
        return creator;
    }
}
