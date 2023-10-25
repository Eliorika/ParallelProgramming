package ru.rsreu.Babaian.synchMech;

public class MyLock {
    private boolean isLocked = false;
    private Object obj = new Object();

    public  void lock() throws InterruptedException {
        synchronized (obj) {
            while (isLocked) {
                obj.wait();
            }
            isLocked = true;
        }
    }

    public void unlock() {
        synchronized(obj) {
            isLocked = false;
            obj.notify();
        }
    }

    public boolean tryLock() {
        synchronized (obj){
            if (!isLocked) {
                isLocked = true;
                return true;
            }
            return false;
        }
    }
}
