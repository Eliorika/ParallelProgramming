package ru.rsreu.Babaian.operations;

import java.util.concurrent.locks.ReentrantLock;

public class IntegralHolder {
    private volatile double integral = 0;


    private Object object = new Object();


    private IntegralHolder(){
        System.out.println("Holder created!");
    }

    private static class LazyHolder{
        static final IntegralHolder INSTANCE = new IntegralHolder();
    }

    public static IntegralHolder getInstance(){
        return LazyHolder.INSTANCE;
    }

    public void addValue(double val){
        synchronized (object){
            this.integral += val;
            object.notify();
        }
    }



    public double getIntegral() throws InterruptedException {
        synchronized (object){
            while(this.integral == 0.0)
                this.wait();
            return this.integral;
        }
    }


}
