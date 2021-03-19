package com.werwolf.helpers;

public class IntroTextCreator extends JSONReader{

    static IntroTextCreator creator = new IntroTextCreator();

    private IntroTextCreator() {
        document = parseFile("src/main/resources/IntroStories.json");
    }

    public static IntroTextCreator getCreator(){
        return creator;
    }
}
