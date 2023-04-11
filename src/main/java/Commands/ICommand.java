package Commands;

interface ICommand {
    public String execute(String text);

    public String getState();
}