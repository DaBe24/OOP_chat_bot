package Commands;

public class Play extends Command {

    @Override
    public String execute(String text) {
        return "List of Game: \n" +
                "/cities - Play Cities (in Russian) whit a bot";
    }
}
