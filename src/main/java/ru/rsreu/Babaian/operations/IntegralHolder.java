package ru.rsreu.Babaian.operations;

public class IntegralHolder {
    private volatile double integral = 0;
    private volatile double progress = 0;
    private volatile double h = 5;

    private final Object obj = new Object();

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
        synchronized (this){
            this.integral += val;
            this.notify();
        }

    }

    public void addProg(double val){
        synchronized (this){
            this.progress += val;
            if (progress > h){
                System.out.println(progress);
                h+=5;
            }

        }
    }

    public double getIntegral() throws InterruptedException {

        synchronized (obj){
            while(this.integral == 0.0)
                this.wait();
            return this.integral;
        }
    }


}
