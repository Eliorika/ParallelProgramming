package ru.rsreu.Babaian;

import ru.rsreu.Babaian.operations.IntegralCalculatorTolerance;

import java.util.HashMap;
import java.util.Map;

public class ThreadContainer {
    private static Map<String, Thread> threads = new HashMap<String, Thread>();
    private static int id = 0;

    public static boolean stopThread(int num){
        if(threads.containsKey(String.valueOf(num))){
            threads.get(String.valueOf(num)).interrupt();
            threads.remove(num);
            return true;
        }
        return false;
    }

    public static String startThread(IntegralCalculatorTolerance integralCalculatorTolerance){
        Thread th = new Thread(integralCalculatorTolerance);
        th.setName(String.valueOf(id));
        th.start();
        threads.put(th.getName(),  th);
        id++;
        return th.getName();
    }

    public static boolean await(int num) throws InterruptedException {
        if(threads.containsKey(String.valueOf(num))){
            threads.get(String.valueOf(num)).join();
            return true;
        }
        return false;
    }

}
