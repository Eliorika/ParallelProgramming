package ru.rsreu.Babaian.commands;

import ru.rsreu.Babaian.ThreadContainer;

public class ExitCommand implements ICommand {

    @Override
    public String process() {
        System.out.println("Program terminated");
        System.exit(0);
        return null;
    }

}
