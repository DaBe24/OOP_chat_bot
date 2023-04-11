package Commands;

public abstract class Command implements ICommand {
    public String name;

    @Override
    public String execute(String text) {
        return null;
    }

    @Override
    public String getState() {
        return "Wait";
    }
}