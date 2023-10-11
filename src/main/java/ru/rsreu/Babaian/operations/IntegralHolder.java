package ru.rsreu.Babaian.operations;

public class IntegralHolder {
    private double integral;

    private static class LazyHolder{
        static final IntegralHolder INSTANCE = new IntegralHolder();
    }

    public static IntegralHolder getInstance(){
        return LazyHolder.INSTANCE;
    }

    public void containValue(double val){
        synchronized (this){
            this.integral = val;
        }

    }

    public double getValue(){
        synchronized (this){
            return this.integral;
        }

    }


}
