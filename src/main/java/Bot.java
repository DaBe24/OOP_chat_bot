import Commands.*;

import java.util.HashMap;


public class Bot {

    private static HashMap<String, Command> commands = new HashMap<String, Command>();

    public Bot(){
        commands.put("/start", new Start());
        commands.put("/help", new Help());
        commands.put("/play", new Play());
        commands.put("/dead_or_alive", new Dead());
    }

    public String takeAnswer(String question) {
        Command handler = commands.get(question);
        return handler != null ? handler.execute(question) : "I don't understand!";
    }
}