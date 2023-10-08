package ru.rsreu.Babaian.commands;

public interface ICommand {
    String process();

    static ICommand determineCommand(String[] command){
        if("start".equals(command[0]))
            return new StartCommand(command[1]);
        else if("stop".equals(command[0]))
            return new StopCommand(command[1]);
        else if("await".equals(command[0]))
            return new AwaitCommand(command[1]);
        else if ("exit".equals(command[0]))
            return new ExitCommand();
        return null;
    }


}
