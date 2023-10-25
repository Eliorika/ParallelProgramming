package ru.rsreu.Babaian.synchMech;

import lombok.Getter;

public class MySemaphore {
    @Getter
    private int permits;
    private final Object obj = new Object();

    public MySemaphore(int permits) {
        this.permits = permits;
    }

    public void acquire() throws InterruptedException {
        synchronized (obj) {
            while (permits == 0) {
                obj.wait();
            }
            permits--;
        }
    }

    public boolean tryAcquire() {
        synchronized (obj) {
            if (permits > 0) {
                permits--;
                return true;
            }
            return false;
        }
    }

    public void release() {
        synchronized (obj) {
            permits++;
            obj.notify();
        }
    }
}
