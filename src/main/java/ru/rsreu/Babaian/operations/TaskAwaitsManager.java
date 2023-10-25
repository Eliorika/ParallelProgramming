package ru.rsreu.Babaian.operations;

import lombok.Setter;
import ru.rsreu.Babaian.synchMech.MyCountDownLatch;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TaskAwaitsManager implements Runnable {
    private static final Lock WRITE_LOCK = new ReentrantReadWriteLock().writeLock();
    private static final ArrayList<Long> TIMES = new ArrayList<>();

    @Setter
    private static MyCountDownLatch countDownLatch;

    public static void countEnded(long time) {
        WRITE_LOCK.lock();
        TIMES.add(time);
        WRITE_LOCK.unlock();
        countDownLatch.countDown();
    }

    @Override
    public void run() {
        try {
            manageTime();
        } catch (InterruptedException ex) {
            System.out.println("TaskAwaitManager stopped");
        }

    }

    private void manageTime() throws InterruptedException {
        countDownLatch.await();

        long endPoint = TIMES.get(TIMES.size() - 1);
        for (int i = 0; i < TIMES.size(); i++) {
            System.out.println("Task " + i + " ended before by " + (endPoint - TIMES.get(i)));
        }
    }
}
