package Commands;

public class Dead extends Command{

    @Override
    public String execute(String text) {
        return "Goodbye. Gleb is out!";
    }

    @Override
    public String getState() {
        return "Dead";
    }
}
