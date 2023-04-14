package Commands;

public class Dead extends Command{

    @Override
    public String execute(String text) {
        return "Hahaha, hero never die!!!";
    }

    @Override
    public String getState() {
        return "Wait";
    }
}
