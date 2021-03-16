package com.werwolf.helpers;

import com.werwolf.game.Game;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserMessageCreator extends JSONReader {

    static UserMessageCreator creator = new UserMessageCreator();

    private UserMessageCreator() {
        document = parseFile("src/main/resources/UserMessages.json");
    }

    //Todo handle language
    public String getMessage(Game game, String title){
        JSONObject message = document.getJSONObject(title);
        if(message.getBoolean("tumMode") && game.getTumMode()){
            return message.getString("englishTUM");
        }
        return message.getString("english");
    }

    public static UserMessageCreator getCreator(){
        return creator;
    }
}
