package ru.rsreu.Babaian.operations;

import java.util.concurrent.locks.ReentrantLock;

public class ProgressReporter {
    private ReentrantLock LOCK = new ReentrantLock();
    private volatile double progress = 0;
    private volatile int h = 5;

    private static class LazyHolder{
        static final ProgressReporter INSTANCE = new ProgressReporter();
    }

    public static ProgressReporter getInstance(){
        return ProgressReporter.LazyHolder.INSTANCE;
    }

    public void addProg(double val){
        LOCK.lock();
        this.progress += val;
        if (progress > h){
            System.out.println(h+"%");
            h+=5;
        }
        LOCK.unlock();
    }
}
