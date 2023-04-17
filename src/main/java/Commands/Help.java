package Commands;

public class Help extends Command{

    @Override
    public String execute(String text) {
        return "Oh hello there! I am Gleb, your text-chat bot.\n" +
                "My command list is:\n" +
                "/help - command list;\n" +
                "/???? - secret command to end current session;\n" +
                "/play - list games;\n";
    }
}
