package ru.rsreu.Babaian.operations;

import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IntegralCalculator implements Runnable{
    @Getter
    private double elapsedTimeSeconds;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private volatile Integer progress = 5;

    public double calculateIntegralParallel(double a, double b, int count) throws InterruptedException {
        int n = 1073741824;
        long startTime = System.nanoTime();
        double h = (b - a) / n;
        int hn = 1073741824 / count;
        double hprog = 100.0/n;

        for (int i = 0; i < count; i++) {
            if(i != count-1)
                calculateIntegralPartFuture(h, i*hn, (i+1)*hn, a, hprog);
            else
                calculateIntegralPartFuture(h, i*hn, n, a, hprog);
        }

        executorService.shutdown();
        while (!executorService.isTerminated()){

        }

        double result = (h / 2.0) * (Math.sin(a) * a + Math.sin(b) * b + 2.0 * IntegralHolder.getInstance().getIntegral());
        long endTime = System.nanoTime();
        this.elapsedTimeSeconds = (endTime - startTime) / 1e9;
        return result;
    }

    private void calculateIntegralPart(double h, int from, int to, double a, double prog) throws InterruptedException {
        double sum = 0.0;
        for (int i = from; i < to; i++) {
            double x = a + i * h;
            sum += Math.sin(x) * x;
            IntegralHolder.getInstance().addProg(prog);

        }

        IntegralHolder.getInstance().addValue(sum);
    }

    private void border(){
        synchronized (this){
            this.progress+=5;
        }
    }

    private Integer getBorder(){
        synchronized (this){
            return this.progress;
        }
    }

    private void calculateIntegralPartFuture(double h, int from, int to, double a, double prog){
        executorService.execute(()-> {
            try {
                calculateIntegralPart(h, from, to, a, prog);
            } catch (InterruptedException e) {
                System.out.println("Thread is interrupted!");
            }
        });
    }

    @Override
    public void run() {

    }
}
