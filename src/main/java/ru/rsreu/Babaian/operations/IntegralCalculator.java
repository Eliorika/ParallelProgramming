package ru.rsreu.Babaian.operations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RequiredArgsConstructor
public class IntegralCalculator implements Runnable{
    private final double tolerance;
    private final int messages = 20;

    @Getter
    private double elapsedTimeSeconds;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public double calculateIntegralFrom0To1() throws InterruptedException, ExecutionException {
        return calculateIntegralWithTolerance(0,1);
    }

    public void writeMessage(int percent){
        System.out.println("Progress: " + percent + "%");
    }

    private double calculateIntegralWithTolerance(double a, double b) throws InterruptedException, ExecutionException {
        double integral = 0.0;
        double previous;
        int n = 1;
        Future<Double> integral1 = calculateIntegralFuture(a, b, n);
        n*=2;
        Future<Double> integral2 = calculateIntegralFuture(a, b, n);


        double progressStep = Math.pow(this.tolerance, 1.0/this.messages);
        double progressBorder = progressStep;
        int percent = 0;
        int percentStep = 100/this.messages;
        writeMessage(percent);

        long startTime = System.nanoTime();

        do {
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            previous = integral;

            integral = integral1.get();
            integral1 = integral2;
            n *= 2;
            integral2 = calculateIntegralFuture(a, b, n);


            if (progressBorder > Math.abs(integral - previous)) {
                percent+=percentStep;
                while (progressBorder * progressStep > Math.abs(integral - previous)) {
                    progressBorder *= progressStep;
                    percent+=percentStep;
                }
                writeMessage(percent);
                progressBorder *= progressStep;
            }
        } while (Math.abs(integral - previous) >= tolerance);

        long endTime = System.nanoTime();
        this.elapsedTimeSeconds = (endTime - startTime) / 1e9;
        return integral;
    }

    private Future<Double> calculateIntegralFuture(double a, double b, int n){
        return executorService.submit(() -> calculateIntegral(a, b, n));
    }

    private double calculateIntegral(double a, double b, int n){
        double h = (b - a) / n;
        double sum = 0.0;
        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += Math.sin(x) * x;
        }
        return (h / 2.0) * (Math.sin(a) * a + Math.sin(b) * b + 2.0 * sum);
    }

    @Override
    public void run() {
        try {
            double answer = calculateIntegralFrom0To1();
            System.out.println("Answer: " + answer);
        } catch (InterruptedException e) {
            System.out.println("Thread is interrupted manually");
        } catch (Exception e){
            System.out.println("Thread is interrupted");
        }
    }
}
