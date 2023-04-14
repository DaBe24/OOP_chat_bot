package Commands;

public class Cities extends Command {

    @Override
    public String execute(String text) {
        return "Strart game \"Города\"\n" +
                "To eng game print \"/EndGame\"\n" +
                "The rules are simple, you need to write the name " +
                "of the city with the same letter as the city " +
                "of the opponent. I will start first\n" +
                "Екатеринбург";
    }

    @Override
    public String getState() {
        return "Play";
    }
}
