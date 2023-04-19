import Commands.*;
import Game.CitiesSolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class Bot {

    private HashMap<String, Command> commands = new HashMap<String, Command>();
    private HashMap<Long, String> chatStatus = new HashMap<Long, String>();
    private HashMap<Long, CitiesSolver> userGame = new HashMap<Long, CitiesSolver>();

    private HashMap<Long, Long> userToUser = new HashMap<Long, Long>();
    private HashMap<Long, Boolean> userMove = new HashMap<>();
    private Boolean needUpdate = false;
    private Boolean needRestart = false;

    public Bot(){
        commands.put("/start", new Start());
        commands.put("/help", new Help());
        commands.put("/play", new Play());
        commands.put("/dead_or_alive", new Dead());
        commands.put("/cities", new Cities());
        commands.put("/playWP", new PlayWithPeople());
    }

    public String takeAnswer(String question, long chat_id) throws IOException {
        if (!chatStatus.containsKey(chat_id)) {
            chatStatus.put(chat_id, "Wait");
        }
        String status = chatStatus.get(chat_id);
        switch (status) {
            case "Wait" -> {
                Command handler = commands.get(question);
                if (handler != null){
                    if (handler.getState().equals("Play")) {
                        startGame(chat_id, handler.getState());
                    }
                    if (handler.getState().equals("PlayWP")) {
                        return startGameWP(chat_id, handler, question);
                    }
                    return handler.execute(question);
                }
                return "I don't understand!";
            }
            case "Play" -> {
                if (question.equals("/EndGame")) {
                    return endGame(chat_id, question, status);
                }
               return checkWordInGame(chat_id, question);
            }
            case "PlayWP" -> {
                if (question.equals("/EndGame")) {
                    return endGame(chat_id, question, status);
                }
                return checkWordInGameWP(chat_id, question);
            }
            default -> {
                return "BED ERROR CRASH!!!\nYou do not have correct status.\nShame on you!";
            }
        }
    }

    private String checkWordInGameWP(long chat_id, String question) throws IOException {
        if (userMove.get(chat_id)){
            CitiesSolver game = userGame.get((long) 1);
            if (game.isCity(question)) {
                if (game.checkRuls(question) && !game.isUsedCity(question)) {
                    needUpdate = true;
                    return "Cool";
                } else {
                    needRestart = true;
                    userGame.put((long) 1, new CitiesSolver());
                    userGame.get((long) 1).isUsedCity("Сургут");
                    return "You lose(";
                }
            } else {
                return "Is not city. Try again";
            }
        }
        return "Wait, not yet your turn.";
    }

    private String checkWordInGame(long chat_id, String question) throws IOException {
        CitiesSolver game = userGame.get(chat_id);
        if (game.isCity(question)) {
            if (game.checkRuls(question) && !game.isUsedCity(question)) {
                String next = game.getNextCity(question);
                if (next.equals("")) {
                    userGame.put(chat_id, new CitiesSolver());
                    userGame.get(chat_id).isUsedCity("Челябинск");
                    return "Damned, You win. ;(\nLet's take one more\nNext Game\nЧелябинск";
                }
                return next;
            } else {
                userGame.put(chat_id, new CitiesSolver());
                userGame.get(chat_id).isUsedCity(question);
                return "YOU LOSE, LOSER!!!\nNext Game\n" + question;
            }
        }
        userGame.put(chat_id, new CitiesSolver());
        userGame.get(chat_id).isUsedCity("Сургут");
        return "YOU LOSE, LOSER!!!\nNext Game\nСургут";
    }

    private String startGameWP(long chat_id, Command handler, String question) throws IOException {
        if (userToUser.size() == 0){
            chatStatus.put(chat_id, handler.getState());
            userGame.put((long) 1, new CitiesSolver());
            userGame.get((long) 1).isUsedCity("Екатеринбург");
            userToUser.put(chat_id, (long) 1);
            userMove.put(chat_id, true);
            return handler.execute(question);
        }
        chatStatus.put(chat_id, handler.getState());
        userToUser.put(chat_id, (long) 1);
        userMove.put(chat_id, false);
        return handler.execute(question) +
                "\n" +
                userGame.get((long) 1).getUsedCitys();
    }

    private void startGame(long chat_id, String status) throws IOException {
        chatStatus.put(chat_id, status);
        userGame.put(chat_id, new CitiesSolver());
        userGame.get(chat_id).isUsedCity("Екатеринбург");
    }

    private String endGame(long chat_id, String question, String status){
        Command handler = new EndGame();
        if (status.equals("Play")) {
            userGame.remove(chat_id);
        } else {
            userToUser.remove(chat_id);
        }
        chatStatus.put(chat_id, handler.getState());
        return handler.execute(question);
    }

    public String takeUpdate(long chat_id, long update_id){
        if (needUpdate){
            List<Long> chat_ids = new ArrayList<>(userToUser.keySet());
            int next_turn =  chat_ids.indexOf(chat_id) == chat_ids.size() - 1 ? 0 : chat_ids.indexOf(chat_id) + 1;

            if (update_id == next_turn){
                userMove.put(update_id, true);
                return userGame.get((long) 1).getLast() + "\n Your turn!";
            }
            if (update_id == chat_id){
                userMove.put(update_id, false);
                return userGame.get((long) 1).getLast();
            }
            return userGame.get((long) 1).getLast();
        }
        if (needRestart){
            if (chat_id == update_id){
                return userGame.get((long) 1).getLast() + "\n Your turn!";
            }
            return "You win. Start next game!\n" + userGame.get((long) 1).getLast();
        }
        return "";
    }

    public List<Long> getChatIds(){
        return needUpdate || needRestart ? new ArrayList<>(userToUser.keySet()) : null;
    }

    public void noUpadete(){
        needUpdate = false;
        needRestart = false;
    }
}