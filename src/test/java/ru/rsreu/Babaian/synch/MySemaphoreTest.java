package ru.rsreu.Babaian.synch;

import org.junit.jupiter.api.Test;
import ru.rsreu.Babaian.synchMech.MySemaphore;

import static org.junit.jupiter.api.Assertions.*;

public class MySemaphoreTest {
    @Test
    void testSemaphoreAcquire() throws InterruptedException {
        MySemaphore semaphore = new MySemaphore(1);
        semaphore.acquire();

        assertEquals(0, semaphore.getPermits());
    }

    @Test
    void testSemaphoreRelease() {
        MySemaphore semaphore = new MySemaphore(0);
        semaphore.release();

        assertEquals(1, semaphore.getPermits());
    }

    @Test
    void testSemaphoreTryAcquire() {
        MySemaphore semaphore = new MySemaphore(1);

        assertTrue(semaphore.tryAcquire());
        assertFalse(semaphore.tryAcquire());
    }

}
