package Commands;

public class PlayWithPeople extends Command{
    @Override
    public String execute(String text) {
        return "Strart of game \"Города\" with people\n" +
                "To eng game print \"/EndGame\"\n" +
                "The rules are simple, you need to write the name " +
                "of the city with the same letter as the city " +
                "of the opponent. I will start first\n" +
                "Екатеринбург\nWait people to game";
    }

    @Override
    public String getState() {
        return "PlayWP";
    }
}
