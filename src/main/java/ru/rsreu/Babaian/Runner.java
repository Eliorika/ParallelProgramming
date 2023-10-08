package ru.rsreu.Babaian;

import ru.rsreu.Babaian.commands.ICommand;

import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(true){
                System.out.println(MenuProcessor.showMenu());
                String[] line = scanner.nextLine().split(" ");
                try {
                    ICommand command = ICommand.determineCommand(line);
                    if (command != null)
                        System.out.println(command.process());
                    else System.out.println("Wrong input!");
                } catch (NumberFormatException e){
                    System.out.println("Wrong input format: " + e.getMessage());
                }
        }
    }
}
