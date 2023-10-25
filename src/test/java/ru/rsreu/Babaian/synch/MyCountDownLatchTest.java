package ru.rsreu.Babaian.synch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rsreu.Babaian.synchMech.MyCountDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyCountDownLatchTest {

    @Test
    void testCountDownMyCountDownLatchTest(){
        int count = 5;
        MyCountDownLatch myCountDownLatch = new MyCountDownLatch(count);
        myCountDownLatch.countDown();

        Assertions.assertEquals(count-1, myCountDownLatch.getCount());
    }

    @Test
    void testCountDownLatchAwait() throws InterruptedException {
        int count = 3;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        MyCountDownLatch myCountDownLatch = new MyCountDownLatch(count);

        Thread th1 = new Thread(() -> {
            System.out.println("THREAD 1 STARTS");
            myCountDownLatch.countDown();
            countDownLatch.countDown();
        });

        Thread th2 = new Thread(() -> {
            System.out.println("THREAD 2 STARTS");
            myCountDownLatch.countDown();
            countDownLatch.countDown();
        });

        Thread th3 = new Thread(() -> {
            System.out.println("THREAD 3 STARTS");
            myCountDownLatch.countDown();
            countDownLatch.countDown();
        });

        th1.start();
        th2.start();
        th3.start();

        myCountDownLatch.await();

        assertEquals(countDownLatch.getCount(), myCountDownLatch.getCount());
    }




}
