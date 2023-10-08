package ru.rsreu.Babaian.operations;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IntegralCalculator implements Runnable{
    private final double tolerance;
    private final int messages = 20;

    @Getter
    private double elapsedTimeSeconds;

    public double calculateIntegralFrom0To1() throws InterruptedException {
        return calculateIntegral(0,1);
    }

    public void writeMessage(int percent){
        System.out.println("Progress: " + percent + "%");
    }

    private double calculateIntegral(double a, double b) throws InterruptedException {
        double integral = 0.0;
        double previous;
        int n = 1;
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
            double h = (b - a) / n;
            double sum = 0.0;

            for (int i = 1; i < n; i++) {
                double x = a + i * h;
                sum += Math.sin(x) * x;
            }

            previous = integral;
            integral = (h / 2.0) * (Math.sin(a) * a + Math.sin(b) * b + 2.0 * sum);

            n *= 2;

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

    @Override
    public void run() {
        try {
            double answer = calculateIntegralFrom0To1();
            System.out.println("Answer: " + answer);
        } catch (InterruptedException e) {
            System.out.println("Thread is interrupted");
        }
    }
}
