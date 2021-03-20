package com.werwolf.helpers;

import com.werwolf.game.Game;
import org.json.JSONObject;

public class UserMessageCreator extends JSONReader {

    static UserMessageCreator creator = new UserMessageCreator();

    private UserMessageCreator() {
        document = parseFile("src/main/data/UserMessages.json");
    }

    public String getMessage(Game game, String title){
        JSONObject message = document.getJSONObject(title);
        if(message.getBoolean("tumMode") && game.getTumMode()){
            if(game.getConfigurations().isEnglish()) return message.getString("englishTUM");
            return message.getString("germanTUM");
        }
        if(game.getConfigurations().isEnglish()) return message.getString("english");
        return message.getString("german");
    }

    public static UserMessageCreator getCreator(){
        return creator;
    }
}
