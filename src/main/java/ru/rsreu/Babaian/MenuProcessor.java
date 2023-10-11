package ru.rsreu.Babaian;

public class MenuProcessor {


    public static String showMenu(){
        String menu = "\nstart <n> - run a task with tolerance n\n" +
                    "stop <n> - stop the task with number n \n" +
                    "await <n> - block terminal till the end of the task with number n\n" +
                    "exit - exit the program";
        return menu;
    }
}
