package ru.rsreu.Babaian.synch;

import org.junit.jupiter.api.Test;
import ru.rsreu.Babaian.synchMech.MyLock;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class MyLockTest {
    @Test
    void testLockAndUnlock() throws InterruptedException {
        MyLock lock = new MyLock();

        lock.lock();

        assertFalse(lock.tryLock());

        lock.unlock();
        assertTrue(lock.tryLock());
    }

    @Test
    void testTryLock() throws InterruptedException {
        MyLock lock = new MyLock();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        assertTrue(lock.tryLock());

        Thread th = new Thread(() -> {
            try {
                lock.lock();
                countDownLatch.await();
                lock.unlock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        th.start();
        assertFalse(lock.tryLock());
        countDownLatch.countDown();
//        th.join();
//        assertTrue(lock.tryLock());
    }
}
