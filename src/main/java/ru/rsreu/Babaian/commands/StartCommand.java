package ru.rsreu.Babaian.commands;

import lombok.Getter;
import ru.rsreu.Babaian.ThreadContainer;
import ru.rsreu.Babaian.operations.IntegralCalculator;

public class StartCommand implements ICommand {

    @Getter
    private double tolerance;

    public StartCommand(String tolerance) throws NumberFormatException{
        this.tolerance = Double.parseDouble(tolerance);
    }

    @Override
    public String process() {
        String name = ThreadContainer.startThread(new IntegralCalculator(this.tolerance));
        return "Task " + name + " started";
    }
}
