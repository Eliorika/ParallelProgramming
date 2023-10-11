package ru.rsreu.Babaian.commands;

public class ExitCommand implements ICommand {

    @Override
    public String process() {
        System.out.println("Program terminated");
        System.exit(0);
        return null;
    }

}
