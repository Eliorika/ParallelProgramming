package ru.rsreu.Babaian.commands;

import lombok.Getter;
import ru.rsreu.Babaian.ThreadContainer;

public class StopCommand implements ICommand {
    @Getter
    private int num;
    public StopCommand(String num) throws NumberFormatException{
        this.num = Integer.parseInt(num);
    }

    @Override
    public String process() {
        if(ThreadContainer.stopThread(this.num)){
            return "Task " + this.num + " stopped";
        }
        return "No such task";
    }
}
