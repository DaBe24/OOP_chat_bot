import Commands.*;
import Game.CitiesSolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;


public class Bot {

    private static HashMap<String, Command> commands = new HashMap<String, Command>();
    private static HashMap<Long, String> chatStatus = new HashMap<Long, String>();
    private static HashMap<Long, CitiesSolver> userGame = new HashMap<Long, CitiesSolver>();

    public Bot(){
        commands.put("/start", new Start());
        commands.put("/help", new Help());
        commands.put("/play", new Play());
        commands.put("/dead_or_alive", new Dead());
        commands.put("/cities", new Cities());
    }

    public String takeAnswer(String question, long chat_id) throws IOException {
        String result;
        if (chatStatus.containsKey(chat_id)){
            if (chatStatus.get(chat_id).equals("Wait")){
                Command handler = commands.get(question);
                if (handler != null && handler.getState().equals("Play")){
                    chatStatus.put(chat_id, handler.getState());
                    userGame.put(chat_id, new CitiesSolver());
                    userGame.get(chat_id).isUsedCity("Екатеринбург");
                }
                return handler != null ? handler.execute(question) : "I don't understand!";
            }
            if (chatStatus.get(chat_id).equals("Play")){
                if (question.equals("/EndGame")){
                    Command handler = new EndGame();
                    userGame.remove(chat_id);
                    chatStatus.put(chat_id, handler.getState());
                    return handler.execute(question);
                }
                CitiesSolver game = userGame.get(chat_id);
                if (game.isCity(question)){
                    boolean a = game.checkRuls(question);
                    boolean b = !game.isUsedCity(question);
                    boolean c = a && b;
                    if (c){
                        return game.getNextCity(question);
                    }else {
                        userGame.put(chat_id, new CitiesSolver());
                        userGame.get(chat_id).isUsedCity(question);
                        return "YOU LOSE, LOSER!!!\nNext Game\n" + question;
                    }
                }
                userGame.put(chat_id, new CitiesSolver());
                userGame.get(chat_id).isUsedCity("Сургут");
                return "YOU LOSE, LOSER!!!\nNext Game\nСургут";
            }
            return "BED ERROR CRASH!!!";
        }
        else {
            Command handler = commands.get(question);
            chatStatus.put(chat_id, handler != null ? handler.getState() : "Wait");
            return handler != null ? handler.execute(question) : "I don't understand!";
        }
    }
}