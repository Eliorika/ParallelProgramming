package ru.rsreu.Babaian.operations;

public class IntegralHolder {
    private double integral;

    private static class LazyHolder{
        static final IntegralHolder INSTANCE = new IntegralHolder();
    }

    public static IntegralHolder getInstance(){
        return LazyHolder.INSTANCE;
    }


}
