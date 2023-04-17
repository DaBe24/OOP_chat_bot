package Commands;

public class Start extends Command {

    @Override
    public String execute(String text) {
        return "I am simple chatbot.\n" + new Help().execute(text);
    }
}
