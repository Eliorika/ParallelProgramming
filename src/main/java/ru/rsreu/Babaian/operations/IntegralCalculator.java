package ru.rsreu.Babaian.operations;

import lombok.Getter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class IntegralCalculator {
    @Getter
    private double elapsedTimeSeconds;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final Semaphore semaphore;

    public IntegralCalculator(int maxTasks) {
        this.semaphore = new Semaphore(maxTasks);
    }

    public double calculateIntegralParallel(double a, double b, int count) throws InterruptedException {
        int n = 1073741824;
        long startTime = System.nanoTime();
        double h = (b - a) / n;
        int hn = n / count;
        double hprog = 100.0/n;
        TaskAwaitsManager.setCountDownLatch(new CountDownLatch(count));
        executorService.execute(new TaskAwaitsManager());

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
        int k = 0;
        int inBorder = 10;
        double inH = 100.0/(to-from);
        double inProg = 0;
        for (int i = from; i < to; i++) {
            double x = a + i * h;
            sum += Math.sin(x) * x;
            k++;
            inProg+=inH;
            if(inProg>=inBorder){
                ProgressReporter.getInstance().addProg(prog*k);
                k=0;
                inBorder+=10;
            }
        }
        ProgressReporter.getInstance().addProg(prog*k);

        IntegralHolder.getInstance().addValue(sum);
        TaskAwaitsManager.countEnded(System.nanoTime());
    }


    private void calculateIntegralPartFuture(double h, int from, int to, double a, double prog){

        executorService.execute(()-> {
            try {
                semaphore.acquire();
                calculateIntegralPart(h, from, to, a, prog);
                semaphore.release();
            } catch (InterruptedException e) {
                System.out.println("Thread is interrupted!");
            }
        });
    }


    public double calculateIntegral(double a, double b, int count) throws InterruptedException {
        int n = 1073741824;
        long startTime = System.nanoTime();
        double sum = 0;
        double h = (b - a) / n;
        int hn = 1073741824 / count;
        double hprog = 100.0/n;


        for (int i = 0; i < n; i++) {
            double x = a + i * h;
            sum += Math.sin(x) * x;

        }

        double result = (h / 2.0) * (Math.sin(a) * a + Math.sin(b) * b + 2.0 * sum);
        long endTime = System.nanoTime();
        this.elapsedTimeSeconds = (endTime - startTime) / 1e9;
        return result;
    }
}
