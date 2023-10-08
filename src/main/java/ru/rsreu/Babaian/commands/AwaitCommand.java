package ru.rsreu.Babaian.commands;

import lombok.Getter;
import ru.rsreu.Babaian.ThreadContainer;

public class AwaitCommand implements ICommand {
    @Getter
    private int num;
    public AwaitCommand(String num) throws NumberFormatException {
        this.num = Integer.parseInt(num);
    }

    @Override
    public String process() {
        try {
            System.out.println("Terminal stopped till the Task " + this.num + " ends");
            if(ThreadContainer.await(this.num)){
                return "Task " + this.num + " ended";
            };
        } catch (InterruptedException e) {
            System.out.println("Task ended with an error");
        }
        return "No such task";
    }
}
