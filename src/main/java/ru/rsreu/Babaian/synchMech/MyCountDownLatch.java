package ru.rsreu.Babaian.synchMech;

public class MyCountDownLatch {
    private int count;
    private final Object obj = new Object();

    public MyCountDownLatch(int count) {
        this.count = count;
    }

    public void countDown() {
        synchronized (obj) {
            if (count > 0) {
                count--;
                if (count == 0) {
                    obj.notifyAll();
                }
            }
        }
    }

    public void await() throws InterruptedException {
        synchronized (obj) {
            while (count > 0) {
                obj.wait();
            }
        }
    }

    public int getCount() {
        synchronized (obj) {
            return count;
        }
    }
}
